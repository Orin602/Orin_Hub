package com.demo.service;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GoogleOauth {

    private final String GOOGLE_SNS_BASE_URL = "https://accounts.google.com/o/oauth2/v2/auth";
    private final String GOOGLE_SNS_CLIENT_ID = "77559460292-67etdiipjfgk8ih2k4l706oog2ghftde.apps.googleusercontent.com";
    private final String GOOGLE_SNS_CALLBACK_URL = "http://localhost:8090/oauth/google/callback";
    private final String GOOGLE_SNS_CLIENT_SECRET = "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";
    private final String GOOGLE_SNS_TOKEN_BASE_URL = "https://oauth2.googleapis.com/token";

    public String getOauthRedirectURL() {
        Map<String, Object> params = new HashMap<>();
        params.put("scope", "profile email");
        params.put("response_type", "code");
        params.put("client_id", GOOGLE_SNS_CLIENT_ID);
        params.put("redirect_uri", GOOGLE_SNS_CALLBACK_URL);

        String parameterString = params.entrySet().stream()
                .map(x -> x.getKey() + "=" + x.getValue())
                .collect(Collectors.joining("&"));

        return GOOGLE_SNS_BASE_URL + "?" + parameterString;
    }

    public Map<String, String> requestAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", GOOGLE_SNS_CLIENT_ID);
        params.add("client_secret", GOOGLE_SNS_CLIENT_SECRET);
        params.add("redirect_uri", GOOGLE_SNS_CALLBACK_URL);
        params.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<Map> responseEntity =
                restTemplate.exchange(GOOGLE_SNS_TOKEN_BASE_URL, HttpMethod.POST, request, Map.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> responseBody = responseEntity.getBody();
            Map<String, String> tokens = new HashMap<>();
            tokens.put("access_token", (String) responseBody.get("access_token"));
            tokens.put("refresh_token", (String) responseBody.get("refresh_token"));
            return tokens;
        }
        throw new RuntimeException("Failed to request access token from Google");
    }

    public String requestAccessTokenUsingURL(String code) {
        try {
            URL url = new URL(GOOGLE_SNS_TOKEN_BASE_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);

            Map<String, Object> params = new HashMap<>();
            params.put("code", code);
            params.put("client_id", GOOGLE_SNS_CLIENT_ID);
            params.put("client_secret", GOOGLE_SNS_CLIENT_SECRET);
            params.put("redirect_uri", GOOGLE_SNS_CALLBACK_URL);
            params.put("grant_type", "authorization_code");

            String parameterString = params.entrySet().stream()
                    .map(x -> x.getKey() + "=" + x.getValue())
                    .collect(Collectors.joining("&"));

            BufferedOutputStream bous = new BufferedOutputStream(conn.getOutputStream());
            bous.write(parameterString.getBytes());
            bous.flush();
            bous.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            if (conn.getResponseCode() == 200) {
                return sb.toString();
            }
            return "구글 로그인 요청 처리 실패";
        } catch (IOException e) {
            throw new IllegalArgumentException("알 수 없는 구글 로그인 Access Token 요청 URL 입니다 :: " + GOOGLE_SNS_TOKEN_BASE_URL);
        }
    }
}
