package com.demo.service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import com.demo.dto.ItemDTO;
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

    public List<ItemDTO> searchBooks(String query, int page) {
        final int resultsPerPage = 10;
        final int startIndex = (page - 1) * resultsPerPage + 1; // 페이지당 시작 인덱스
        
        // API 요청 URL 생성
        String url = String.format("https://www.nl.go.kr/NL/search/openApi/search.do?key=%s&kwd=%s&detailSearch=true&f1=title&start=%d&maxResults=%d&apiType=json&category=도서",
                apiKey,
                query,
                startIndex,
                resultsPerPage);

        // API 호출
        String response = restTemplate.getForObject(url, String.class);
        System.out.println("API Response: " + response); // 응답 확인용

        List<ItemDTO> items = new ArrayList<>();
        try {
            if (response != null && !response.isEmpty()) {
                JsonNode root = objectMapper.readTree(response);
                JsonNode resultNode = root.path("result"); // JSON 응답에서 'result' 노드에 접근

                for (JsonNode itemNode : resultNode) {
                    ItemDTO item = new ItemDTO();
                    item.setTitleInfo(itemNode.path("titleInfo").asText().replaceAll("<[^>]*>", "").trim()); // HTML 태그 제거
                    item.setAuthorInfo(itemNode.path("authorInfo").asText().replaceAll("<[^>]*>", "").trim()); // HTML 태그 제거
                    
                    // 이미지 URL 처리
                    String imageUrl = itemNode.path("imageUrl").asText().trim();
                    if (imageUrl.isEmpty()) {
                        imageUrl = "/images/no_img.jpg"; // 기본 이미지 URL 설정
                    } else {
                        imageUrl = "http://cover.nl.go.kr/" + imageUrl;
                    }
                    item.setImageUrl(imageUrl);
                    items.add(item);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return items;
    }
}
