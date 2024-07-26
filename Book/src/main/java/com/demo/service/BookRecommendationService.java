package com.demo.service;

import java.io.StringReader;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.demo.dto.BookRecommendationResponse;
import com.demo.dto.BookRecommendation;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

@Service
public class BookRecommendationService {

    @Value("${library.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public BookRecommendationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<BookRecommendation> getBookRecommendations() throws JAXBException {
        String url = "https://nl.go.kr/NL/search/openApi/saseoApi.do?key=" + apiKey;
        String response = restTemplate.getForObject(url, String.class);

        // 로그 추가
        System.out.println("Response: " + response);

        JAXBContext jaxbContext = JAXBContext.newInstance(BookRecommendationResponse.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        BookRecommendationResponse bookRecommendationResponse = (BookRecommendationResponse) unmarshaller.unmarshal(new StringReader(response));
        return bookRecommendationResponse.getList();
    }
}
