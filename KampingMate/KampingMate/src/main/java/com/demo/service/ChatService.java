package com.demo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

@Service
public class ChatService {

    @Value("${kakao.api.key}")
    private String apiKey;

    private static final String API_URL = "https://api.kakaobrain.com/v1/inference/kogpt/generation";
    private final RestTemplate restTemplate = new RestTemplate();
    private final RetryTemplate retryTemplate;

    private final Map<String, String> predefinedResponses = new HashMap<>();

    public ChatService(RetryTemplate retryTemplate) {
        this.retryTemplate = retryTemplate;
        initializePredefinedResponses();
    }

    private void initializePredefinedResponses() {
        predefinedResponses.put("Kampingmate란 ?", "사용자에 맞춰 캠핑장을 추천해주고 캠핑장의 리뷰와 위치등 다양한 캠핑관련정보를 제공하는 웹사이트입니다.");
        predefinedResponses.put("캠핑이 처음이야", "캠핑이 처음이시라면 가이드메뉴를 통해 캠핑준비를 차근차근 해보시는것을 추천드립니다.");
        predefinedResponses.put("캠핑장 추천", "캠핑장 추천은 일반 검색을 통해 받기 또는 내 활동기반 추천 받기 두가지가 가능합니다.");
        predefinedResponses.put("불만이 있어", "고객센터에서 1:1문의를 작성 해 주세요");
        predefinedResponses.put("이전에 추천받았던 내용이 궁금해", "마이페이지에 들어가시면 이전 기록을 확인하실 수 있습니다.");
    }

    @Retryable(value = {org.springframework.web.client.HttpServerErrorException.ServiceUnavailable.class}, maxAttempts = 5, backoff = @Backoff(delay = 5000))
    public String getChatbotResponse(String userMessage) {

        if (predefinedResponses.containsKey(userMessage)) {
            return predefinedResponses.get(userMessage);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + apiKey);
        headers.set("Content-Type", "application/json");

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("prompt", "질문: " + userMessage + "\n답변:");
        requestBody.put("max_tokens", 45);
        requestBody.put("temperature", 0.2); 
        requestBody.put("top_p", 0.9);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = retryTemplate.execute(context -> restTemplate.exchange(API_URL, HttpMethod.POST, entity, String.class));
            Map<String, Object> responseBody = new Gson().fromJson(response.getBody(), Map.class);
            if (responseBody.containsKey("generations")) {
                List<Map<String, Object>> generations = (List<Map<String, Object>>) responseBody.get("generations");
                if (!generations.isEmpty() && generations.get(0).containsKey("text")) {
                    String generatedText = generations.get(0).get("text").toString();
                    return postProcessResponse(generatedText);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "죄송합니다. 이해하지 못했습니다.";
    }

    private String postProcessResponse(String response) {
        // 필요에 따라 응답을 후처리하여 불필요한 부분 제거
        response = response.replaceAll("\\s+", " ").trim();
        return response;
    }
}
