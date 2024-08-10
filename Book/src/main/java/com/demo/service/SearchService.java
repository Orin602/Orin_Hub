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

	private static final Logger logger = LoggerFactory.getLogger(SearchService.class);
	
    @Value("${aladin.api.key}")
    private String ttbKey;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private static final String ALADIN_API_URL = "http://www.aladin.co.kr/ttb/api/ItemSearch.aspx";

    public SearchService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public List<ItemDTO> searchBooks(String query, int start) {
        final int resultsPerPage = 10;

        // API 요청 URL 생성
        String url = String.format("%s?ttbkey=%s&Query=%s&QueryType=Title&Version=20131101&SearchTarget=Book&MaxResults=%d&start=%d&sort=Accuracy&output=js",
                ALADIN_API_URL,
                ttbKey,
                query, // 쿼리 값
                resultsPerPage,
                start);
        // API 호출
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        String response = responseEntity.getBody();

        // 로그 추가: API 요청 URL과 응답 확인
        logger.info("API Request URL: {}", url);
        logger.info("API Response: {}", response);

        List<ItemDTO> items = new ArrayList<>();

        try {
            if (response == null || response.isEmpty()) {
                logger.warn("No response received from API.");
            } else {
                JsonNode root = objectMapper.readTree(response);
                JsonNode itemsNode = root.path("item");

                for (JsonNode itemNode : itemsNode) {
                    ItemDTO item = objectMapper.treeToValue(itemNode, ItemDTO.class);
                    items.add(item);
                }
            }
        } catch (Exception e) {
            logger.error("Error parsing API response: {}", e.getMessage(), e);
        }

        return items;
    }

}
