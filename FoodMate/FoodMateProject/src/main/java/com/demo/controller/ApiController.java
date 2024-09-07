package com.demo.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.domain.CommunityBoard;
import com.demo.service.ChatService;
import com.demo.service.CommunityBoardService;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

@RestController
public class ApiController {

    @Autowired
    private ChatService chatService;
    
    @Autowired
    private CommunityBoardService communityService;

    @PostMapping("/api/chat/send")
    public String sendMessage(@RequestParam String message) {
        return chatService.getChatbotResponse(message);
    }
    
    @GetMapping("/api/generate-pdf")
    public ResponseEntity<byte[]> generatePdf(@RequestParam Map<String, String> params) {
        String communitySeq = params.get("community_seq");
        CommunityBoard communityBoard = communityService.getCommunityBoardByCommunity_seq(communitySeq);
        String title = communityBoard.getCommunity_title();
        
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
                "<h2>" + communityBoard.getCommunity_title() + "</h2>" +
                "<p>" + communityBoard.getCommunity_content() + "</p>" +
                "<span>작성자: " + communityBoard.getMember_data().getId() + "</span>" +
                "<span>작성일: " + communityBoard.getCommunity_d_regdate() + "</span>" +
                "<span>조회수: " + communityBoard.getCommunity_cnt() + "</span>" +
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
}