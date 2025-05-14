package com.demo.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class KakaoApiController {

	@Value("${kakao.api.key}")
	private String kakaoApiKey;

	@GetMapping("/api/kakao/address")
	public ResponseEntity<?> searchAddress(@RequestParam String keyword) {
		try {
			// 1. Kakao API URL 구성
			String apiUrl = "https://dapi.kakao.com/v2/local/search/keyword.json?query="
					+ URLEncoder.encode(keyword, "UTF-8");

			// 2. HTTP 연결 설정
			HttpURLConnection conn = (HttpURLConnection) new URL(apiUrl).openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Authorization", "KakaoAK " + kakaoApiKey);

			// 3. 응답 코드 확인
			int responseCode = conn.getResponseCode();

			if (responseCode == 200) {
				// 4. 응답 데이터 읽기 (try-with-resources로 자동 닫기)
				try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
					StringBuilder responseBuilder = new StringBuilder();
					String line;
					while ((line = reader.readLine()) != null) {
						responseBuilder.append(line);
					}

					// 5. JSON 문자열을 객체로 변환
					ObjectMapper objectMapper = new ObjectMapper();
					Object json = objectMapper.readValue(responseBuilder.toString(), Object.class);
					return ResponseEntity.ok(json);
				}

			} else {
				return ResponseEntity.status(responseCode).body("Kakao 키워드 검색 API 호출 실패 (응답 코드: " + responseCode + ")");
			}

		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body("서버 오류: " + e.getMessage());
		}
	}
}
