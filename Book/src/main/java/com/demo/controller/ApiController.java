package com.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ApiController {

	@Value("${library.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public ApiController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    @GetMapping("/proxy/library")
    public ResponseEntity<String> proxyLibraryApi(@RequestParam String key) {
        String url = "https://nl.go.kr/NL/search/openApi/saseoApi.do?key=" + key;
        String response = restTemplate.getForObject(url, String.class);
        return ResponseEntity.ok(response);
    }
}
