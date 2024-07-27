package com.demo.service;

import com.demo.dto.SearchResultDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SearchService {

    @Value("${aladin.api.key}")
    private String ttbKey;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private static final String ALADIN_API_URL = "http://www.aladin.co.kr/ttb/api/ItemSearch.aspx";

    public SearchService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public SearchResultDTO searchBooks(String query, int start) {
        // API 요청 URL 생성
        String url = UriComponentsBuilder.fromHttpUrl(ALADIN_API_URL)
            .queryParam("ttbkey", ttbKey)         // TTB Key
            .queryParam("Query", query)           // 검색어
            .queryParam("QueryType", "Keyword")   // 검색 타입 (기본값)
            .queryParam("SearchTarget", "Book")   // 검색 대상 (기본값)
            .queryParam("MaxResults", 10)          // 최대 결과 수
            .queryParam("start", start)            // 시작 페이지
            .queryParam("sort", "Accuracy")       // 정렬 순서 (기본값)
            .queryParam("output", "js")            // 응답 형식 (JSON)
            .toUriString();

        // API 호출
        String response = restTemplate.getForObject(url, String.class);

        // JSON 응답을 DTO로 변환
        try {
            return objectMapper.readValue(response, new TypeReference<SearchResultDTO>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return new SearchResultDTO(); // 오류 발생 시 빈 결과 반환
        }
    }
}
