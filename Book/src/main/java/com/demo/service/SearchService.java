package com.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.demo.dto.ItemDTO;
import com.demo.dto.SearchResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SearchService {

//	private static final Logger logger = LoggerFactory.getLogger(SearchService.class);
//	
//    @Value("${aladin.api.key}")
//    private String ttbKey;
//
//    private final RestTemplate restTemplate;
//    private final ObjectMapper objectMapper;
//
//    private static final String ALADIN_API_URL = "http://www.aladin.co.kr/ttb/api/ItemSearch.aspx";
//
//    public SearchService(RestTemplate restTemplate, ObjectMapper objectMapper) {
//        this.restTemplate = restTemplate;
//        this.objectMapper = objectMapper;
//    }
//
//    public List<ItemDTO> searchBooks(String query, int start) {
//        final int resultsPerPage = 10;
//
//        // API 요청 URL 생성
//        String url = String.format("%s?ttbkey=%s&Query=%s&QueryType=Title&Version=20131101&SearchTarget=Book&MaxResults=%d&start=%d&sort=Accuracy&output=js",
//                ALADIN_API_URL,
//                ttbKey,
//                query, // 쿼리 값
//                resultsPerPage,
//                start);
//        // API 호출
//        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
//        String response = responseEntity.getBody();
//
//        // 로그 추가: API 요청 URL과 응답 확인
//        logger.info("API Request URL: {}", url);
//        logger.info("API Response: {}", response);
//
//        List<ItemDTO> items = new ArrayList<>();
//
//        try {
//            if (response == null || response.isEmpty()) {
//                logger.warn("No response received from API.");
//            } else {
//                JsonNode root = objectMapper.readTree(response);
//                JsonNode itemsNode = root.path("item");
//
//                for (JsonNode itemNode : itemsNode) {
//                    ItemDTO item = objectMapper.treeToValue(itemNode, ItemDTO.class);
//                    items.add(item);
//                }
//            }
//        } catch (Exception e) {
//            logger.error("Error parsing API response: {}", e.getMessage(), e);
//        }
//
//        return items;
//    }
	private static final Logger logger = LoggerFactory.getLogger(SearchService.class);

    @Value("${library.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private static final String NLA_API_URL = "https://nlapi.nl.go.kr/api/collection/search";

    public SearchService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

//    public List<ItemDTO> searchBooks(String query, int page, String srchTarget) {
//        final int resultsPerPage = 10; // 페이지당 결과 수
//        final int startIndex = (page - 1) * resultsPerPage + 1; // 페이지 시작 인덱스 계산
//        
//        // 검색 조건 설정
//        String searchField = srchTarget.equals("title") ? "title" : srchTarget.equals("author") ? "author" : "total";
//        
//        // URL 생성 (pageNum을 startIndex로 변환)
//        String url = String.format("https://www.nl.go.kr/NL/search/openApi/search.do?key=%s&kwd=%s&detailSearch=true&f1=%s&start=%d&maxResults=%d&apiType=json&category=도서",
//                apiKey, query, searchField, startIndex, resultsPerPage);
//        
//        // URL 출력
//        System.out.println("API 요청 URL: " + url);
//
//        // API 호출
//        String response = restTemplate.getForObject(url, String.class);
//        System.out.println("API Response: " + response); // 응답 확인용
//
//        List<ItemDTO> items = new ArrayList<>();
//        try {
//            if (response != null && !response.isEmpty()) {
//                JsonNode root = objectMapper.readTree(response);
//                JsonNode resultNode = root.path("result"); // JSON 응답에서 'result' 노드에 접근
//
//                for (JsonNode itemNode : resultNode) {
//                    ItemDTO item = new ItemDTO();
//                    item.setTitleInfo(itemNode.path("titleInfo").asText().replaceAll("<[^>]*>", "").trim());
//                    item.setAuthorInfo(itemNode.path("authorInfo").asText().replaceAll("<[^>]*>", "").trim());
//
//                    // 이미지 URL 처리
//                    String imageUrl = itemNode.path("imageUrl").asText().trim();
//                    if (imageUrl.isEmpty()) {
//                        imageUrl = "/images/no_img.jpg"; // 기본 이미지 URL 설정
//                    } else {
//                        imageUrl = "http://cover.nl.go.kr/" + imageUrl;
//                    }
//                    item.setImageUrl(imageUrl);
//                    items.add(item);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return items;
//    }
    
    public SearchResult searchBooks(String query, int pageNum, String srchTarget) {
        final int resultsPerPage = 10; // 페이지당 결과 수
        final int startIndex = (pageNum - 1) * resultsPerPage + 1; // start를 계산 (1부터 시작)

        // 검색 조건 설정
        String searchField = srchTarget.equals("title") ? "title" : srchTarget.equals("author") ? "author" : "total";

        String url = String.format("https://www.nl.go.kr/NL/search/openApi/search.do?key=%s&kwd=%s&detailSearch=true&f1=%s&pageNum=%d&maxResults=%d&apiType=json&category=도서",
                apiKey, query, searchField, pageNum, resultsPerPage);

        // API 호출
        String response = restTemplate.getForObject(url, String.class);

        List<ItemDTO> items = new ArrayList<>();
        int totalPages = 0;  // 전체 페이지 수
        int totalResults = 0;  // 전체 검색 결과 수

        try {
            if (response != null && !response.isEmpty()) {
                JsonNode root = objectMapper.readTree(response);
                totalResults = root.path("total").asInt(); // 전체 검색 결과 수
                totalPages = totalResults > 0 ? (int) Math.ceil((double) totalResults / resultsPerPage) : 0; // 전체 페이지 수 계산

                JsonNode resultNode = root.path("result");
                for (JsonNode itemNode : resultNode) {
                    ItemDTO item = new ItemDTO();
                    item.setTitleInfo(itemNode.path("titleInfo").asText().replaceAll("<[^>]*>", "").trim());
                    item.setAuthorInfo(itemNode.path("authorInfo").asText().replaceAll("<[^>]*>", "").trim());
                    String imageUrl = itemNode.path("imageUrl").asText().trim();
                    item.setImageUrl(imageUrl.isEmpty() ? "/images/no_img.jpg" : "http://cover.nl.go.kr/" + imageUrl);
                    items.add(item);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new SearchResult(items, pageNum, totalPages, totalResults); // searchResult 반환
    }




}
