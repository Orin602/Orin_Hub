package com.demo.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.demo.domain.Book;
import com.demo.service.BookService;
import com.demo.service.ChatService;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Value("${api.service.key1}")
    private String serviceKey;
    
    @Autowired
    private BookService booksv;
    
    @Autowired
    private ChatService chatService;

    
    private static final Logger logger = Logger.getLogger(ApiController.class.getName());

    @GetMapping("/data")
    public ResponseEntity<String> callApi() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://apis.data.go.kr/B551011/GoCamping/locationBasedList";
        URI uri = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("serviceKey", serviceKey)
                .queryParam("numOfRows", 99) // 한페이지결과수
                .queryParam("pageNo", 1) // 페이지번호
                .queryParam("MobileOS", "ETC")
                .queryParam("MobileApp", "AppTest")
                .queryParam("mapX", 128.6142847) // 경도
                .queryParam("mapY", 36.0345423) // 위도
                .queryParam("radius", 2000) // 거리 반경
                .build()
                .toUri();

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return response;
            } else {
                logger.severe("Failed to call API: " + response.getStatusCode());
                return new ResponseEntity<>("Failed to call API", response.getStatusCode());
            }
        } catch (RestClientException e) {
            logger.severe("Exception occurred while calling API: " + e.getMessage());
            return new ResponseEntity<>("Exception occurred while calling API: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    
    @GetMapping("/generate-pdf")
    public ResponseEntity<byte[]> generatePdf(@RequestParam Map<String, String> params) {
    	int book_seq = Integer.parseInt(params.get("book_seq"));
        Book book = booksv.getBook(book_seq);
        String title = book.getCampingname() + "예약일정";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        String fontPath = new File("src/main/resources/static/fonts/NanumBarunGothic.ttf").getAbsolutePath();

        // HTML 콘텐츠 생성
        String htmlContent = "<html><head>" +
                "<style>" +
                "@font-face {" +
                "    font-family: 'NanumBarunGothic';" +
                "    src: url('file:///" + fontPath.replace("\\", "/") + "');" +
                "}" +
                "body { font-family: 'NanumBarunGothic'; }" +
                "</style>" +
                "</head><body>" +
                "<h2>" + sdf.format(book.getBookdateS()) + "~" + sdf.format(book.getBookdateE()) + "</h2><br />" +
                "<span>캠핑장명: " + book.getCampingname() + "</span><br />" +
                "<span>인원수: " + book.getHeadcount() + "</span><br />" + 
                "<span>작성자: " + book.getMember_data().getId() + "</span><br />" +
                "<span>예약관련 메시지: " + book.getMessage() + "</span>" +
                "</body></html>";

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFont(new File(fontPath), "NanumBarunGothic");
            builder.withHtmlContent(htmlContent, null);
            builder.toStream(byteArrayOutputStream);
            builder.run();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }

        byte[] pdfBytes = byteArrayOutputStream.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String encodedTitle;
        try {
            encodedTitle = URLEncoder.encode(title, "UTF-8").replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            encodedTitle = title;
        }
        headers.setContentDispositionFormData("attachment", encodedTitle + ".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }
    
    
    @PostMapping("/chat/send")
    public String sendMessage(@RequestParam String message) {
        return chatService.getChatbotResponse(message);
    }
}


