package com.demo.service;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.demo.dto.BookRecommendation;
import com.demo.dto.BookRecommendationResponse;
import com.demo.dto.BookRecommendationWrapper;

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

    public List<BookRecommendation> getBookRecommendations(String start_date, String end_date) throws JAXBException {
        String url = String.format("https://nl.go.kr/NL/search/openApi/saseoApi.do?key=%s&start_date=%s&end_date=%s", 
                apiKey, start_date, end_date);
        String response = restTemplate.getForObject(url, String.class);

        // 로그 추가
        System.out.println("Response: " + response);

        JAXBContext jaxbContext = JAXBContext.newInstance(BookRecommendationResponse.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        BookRecommendationResponse bookRecommendationResponse = 
            (BookRecommendationResponse) unmarshaller.unmarshal(new StringReader(response));

        // 모든 리스트의 아이템들을 하나의 리스트로 합치기
        List<BookRecommendation> recommendations = new ArrayList<>();
        for (BookRecommendationWrapper listWrapper : bookRecommendationResponse.getLists()) {
            recommendations.addAll(listWrapper.getItems());
        }
        // 추천 목록이 비어있는 경우
        if (recommendations.isEmpty()) {
            return Collections.singletonList(new BookRecommendation()); // 빈 객체를 반환
        }
        return recommendations;
    }
}
