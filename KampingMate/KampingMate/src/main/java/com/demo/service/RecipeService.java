package com.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class RecipeService {

    private final String apiKey = "863b0dafc8cc4d50b8c4";
    private final String apiUrl = "http://openapi.foodsafetykorea.go.kr/api/" + apiKey + "/COOKRCP01/json/1/10";

    public Map<String, Object> getRecipes() {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> response = restTemplate.getForObject(apiUrl, Map.class);
        return response;
    }
    
}
