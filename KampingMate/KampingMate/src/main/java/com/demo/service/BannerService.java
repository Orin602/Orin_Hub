package com.demo.service;

import com.demo.dto.BannerItem;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BannerService {

    private final RestTemplate restTemplate;

    private final String clientId = "Nu1zu0YqP_f3ovT13oeU";
    private final String clientSecret = "ZsC_OIBy3d";

    public BannerService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<BannerItem> getBannerItems() {
        String url = "https://openapi.naver.com/v1/search/shop.json?query=캠핑용품&display=30&sort=sim";
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", clientSecret);
        
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            List<Map<String, Object>> items = (List<Map<String, Object>>) response.getBody().get("items");
            return items.stream().map(this::mapToBannerItem).collect(Collectors.toList());
        } else {
            throw new RuntimeException("Failed to fetch items from Naver API");
        }
    }

    private BannerItem mapToBannerItem(Map<String, Object> item) {
        return new BannerItem(
            (String) item.get("title"),
            (String) item.get("link"),
            (String) item.get("image"),
            (String) item.get("lprice"),
            (String) item.get("hprice"),
            (String) item.get("mallName"),
            (String) item.get("productId"),
            (String) item.get("productType"),
            (String) item.get("brand"),
            (String) item.get("maker"),
            (String) item.get("category1"),
            (String) item.get("category2"),
            (String) item.get("category3"),
            (String) item.get("category4")
        );
    }
}
