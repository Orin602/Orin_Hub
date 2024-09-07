package com.demo.service;

import java.io.IOException;
import java.util.Map;

import com.demo.dto.GoogleUser;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class OauthService {

    private final RestTemplate restTemplate;

    // 구글 OAuth2 설정 값들
    private final String googleUserinfoUrl = "https://www.googleapis.com/oauth2/v1/userinfo";
    private final String clientId = "77559460292-67etdiipjfgk8ih2k4l706oog2ghftde.apps.googleusercontent.com";
    private final String clientSecret = "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";
    private final String tokenUrl = "https://oauth2.googleapis.com/token";
    private final String redirectUri = "http://localhost:8090/oauth/google/callback";

    public OauthService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String requestGoogleLogin(HttpServletResponse response) {
        String redirectURL = "https://accounts.google.com/o/oauth2/v2/auth?" +
                "client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&response_type=code" +
                "&scope=openid%20email%20profile";

        try {
            response.sendRedirect(redirectURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<String, String> requestAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", code);
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("redirect_uri", redirectUri);
        body.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return (Map<String, String>) response.getBody();
        } else {
            throw new RuntimeException("Failed to get access token");
        }
    }

    public GoogleUser getGoogleUserInfo(String accessToken) {
        String url = googleUserinfoUrl + "?access_token=" + accessToken;
        return restTemplate.getForObject(url, GoogleUser.class);
    }

    public String refreshAccessToken(String refreshToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("refresh_token", refreshToken);
        body.add("grant_type", "refresh_token");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, String> responseBody = (Map<String, String>) response.getBody();
            return responseBody.get("access_token");
        } else {
            throw new RuntimeException("Failed to refresh access token");
        }
    }
}
