package com.demo.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service
public class NApiService {

    private String clientId = "XRBS7ec56NR7kYokekFO"; // 애플리케이션 클라이언트 아이디
    private String clientSecret = "FYr1SnOTYE"; // 애플리케이션 클라이언트 시크릿

    public String searchNews(String query) {
        String text;
        try {
            text = URLEncoder.encode(query, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("검색어 인코딩 실패", e);
        }

        String apiURL = "https://openapi.naver.com/v1/search/news?query=" + text;

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", clientId);
        requestHeaders.put("X-Naver-Client-Secret", clientSecret);

        String response = get(apiURL, requestHeaders);
        
        // JSON 데이터를 보기 쉽게 포맷팅하고 HTML로 변환
        return formatToJsonAndHtml(response);
    }

    private String get(String apiUrl, Map<String, String> requestHeaders) {
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(con.getInputStream());
            } else { // 오류 발생
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

    private HttpURLConnection connect(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection) url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }

    private String readBody(InputStream body) {
        InputStreamReader streamReader = new InputStreamReader(body);

        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();

            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }

            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는 데 실패했습니다.", e);
        }
    }

//    private String formatToJsonAndHtml(String json) {
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        JsonObject jsonObj = JsonParser.parseString(json).getAsJsonObject();
//        JsonArray items = jsonObj.getAsJsonArray("items");
//
//        StringBuilder htmlBuilder = new StringBuilder();
//        htmlBuilder.append("<table border='1'>");
//        htmlBuilder.append("<tr><th>뉴스 제목</th><th>뉴스 내용</th><th>바로가기</th><th>날짜</th></tr>");
//        
//        for (int i = 0; i < items.size(); i++) {
//            JsonObject item = items.get(i).getAsJsonObject();
//            String title = item.get("title").getAsString();
//            String description = item.get("description").getAsString();
//            String link = item.get("link").getAsString();
//            String pubDate = item.get("pubDate").getAsString();
//            
//            htmlBuilder.append("<tr>");
//            htmlBuilder.append("<td>").append(title).append("</td>");
//            htmlBuilder.append("<td>").append(description).append("</td>");
//            htmlBuilder.append("<td><a href='").append(link).append("' target='_blank'>Link</a></td>");
//            htmlBuilder.append("<td>").append(pubDate).append("</td>");
//            htmlBuilder.append("</tr>");
//        }
//        
//        htmlBuilder.append("</table>");
//        return htmlBuilder.toString();
//    }
    private String formatToJsonAndHtml(String json) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jsonObj = JsonParser.parseString(json).getAsJsonObject();
        JsonArray items = jsonObj.getAsJsonArray("items");

        List<Map<String, String>> newsList = new ArrayList<>();

        for (int i = 0; i < items.size(); i++) {
            JsonObject item = items.get(i).getAsJsonObject();
            Map<String, String> newsItem = new HashMap<>();
            newsItem.put("title", item.get("title").getAsString());
            newsItem.put("description", item.get("description").getAsString());
            newsItem.put("link", item.get("link").getAsString());
            newsItem.put("pubDate", item.get("pubDate").getAsString());
            newsList.add(newsItem);
        }

        return gson.toJson(newsList);
    }


    private List<JsonObject> getPageItems(JsonArray items, int pageNum, int pageSize) {
        List<JsonObject> pageItems = new ArrayList<>();
        int startIndex = (pageNum - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, items.size());

        for (int i = startIndex; i < endIndex; i++) {
            pageItems.add(items.get(i).getAsJsonObject());
        }

        return pageItems;
    }
    public static void main(String[] args) {
        NApiService apiService = new NApiService();
        String response = apiService.searchNews("캠핑");
        System.out.println(response);
    }
}
