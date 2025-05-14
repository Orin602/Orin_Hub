package com.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.demo.dto.ItemDTO;
import com.demo.dto.SearchResultDTO;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AladinApiService {

	@Value("${aladin.api.key}")
	private String apikey;

	// 알라딘 검색 요청 URL
	private static final String API_URL = 
			"https://www.aladin.co.kr/ttb/api/ItemSearch.aspx?TTBKEY=%s&Query=%s&QueryType=%s&SearchTarget=book&Output=js&Sort=%s&start=%d";

	private final RestTemplate restTemplate;

	// RestTemplate을 의존성 주입 받기
	public AladinApiService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public SearchResultDTO searchBooks(String query, String queryType, String sort, int page) {
	    String url = String.format(API_URL, apikey, query, queryType, sort, (page - 1) * 10 + 1);

	    List<ItemDTO> items = new ArrayList<>();
	    ObjectMapper objectMapper = new ObjectMapper();
	    int totalResults = 0;

	    // Jackson 설정
	    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	    objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
	    objectMapper.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);

	    try {
	        String response = restTemplate.getForObject(url, String.class);

	        if (response != null && !response.isEmpty()) {
	            response = response.replaceAll("\\\\'", "'");

	            JsonNode root = objectMapper.readTree(response);
	            totalResults = root.path("totalResults").asInt();

	            JsonNode resultNode = root.path("item");

	            for (JsonNode itemNode : resultNode) {
	                ItemDTO item = new ItemDTO();
	                item.setTitle(itemNode.path("title").asText().replaceAll("<[^>]*>", "").trim());
	                item.setAuthor(itemNode.path("author").asText().replaceAll("<[^>]*>", "").trim());
	                item.setPricesales(itemNode.path("priceSales").asInt());
	                item.setPricestandard(itemNode.path("priceStandard").asInt());

	                String imageUrl = itemNode.path("cover").asText().trim();
	                item.setCover(imageUrl.isEmpty() ? "/images/no_img.jpg" : imageUrl);
	                item.setCustomerReviewRank(itemNode.path("customerReviewRank").asInt());

	                items.add(item);
	            }

	            // ⛔ totalResults 보정 로직 추가: 실제 마지막 페이지면 그걸로 조정
	            if (items.size() < 10) {
	                totalResults = (page - 1) * 10 + items.size();
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return new SearchResultDTO(items, totalResults);
	}

}
