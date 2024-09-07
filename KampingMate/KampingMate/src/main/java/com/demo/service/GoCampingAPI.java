package com.demo.service;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.demo.dto.CampingItem;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

@Service
public class GoCampingAPI {
    private static final String SERVICE_KEY = "fnb%2FPvsKoyOP9rj3vWkyGBtUun17J%2BDKEakIEuGJgj6Swq1nm6ILNuPBpz0S6hmW3sI8Jc54%2FR%2FB1k5heAIuig%3D%3D";
    private static final String ENDPOINT = "http://apis.data.go.kr/B551011/GoCamping/basedList";

    public static class CampingApiResponse {
        private int totalCount;
        private List<CampingItem> items;

        public CampingApiResponse(int totalCount, List<CampingItem> items) {
            this.totalCount = totalCount;
            this.items = items;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public List<CampingItem> getItems() {
            return items;
        }
    }

    public CampingApiResponse getCampingSites(int page, int pageSize) throws Exception {
        String urlStr = ENDPOINT +
                "?serviceKey=" + SERVICE_KEY +
                "&numOfRows=" + pageSize +
                "&pageNo=" + page +
                "&MobileOS=AND" +
                "&MobileApp=appName" +
                "&_type=json";
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        int responseCode = conn.getResponseCode();
        System.out.println("Response Code : " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { // 200
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Parse JSON response
            JSONObject jsonObj = new JSONObject(response.toString());
            JSONObject responseBody = jsonObj.getJSONObject("response").getJSONObject("body");

            // totalCount
            int totalCount = responseBody.getInt("totalCount");

            // items에서 필요한 정보 추출
            List<CampingItem> items = new ArrayList<>();
            JSONArray jsonItems = responseBody.getJSONObject("items").getJSONArray("item");
            for (int i = 0; i < jsonItems.length(); i++) {
                JSONObject item = jsonItems.getJSONObject(i);
                
                // Aggregating room counts
                int gnrlSiteCo = item.optInt("gnrlSiteCo", 0);
                int autoSiteCo = item.optInt("autoSiteCo", 0);
                int glampSiteCo = item.optInt("glampSiteCo", 0);
                int caravSiteCo = item.optInt("caravSiteCo", 0);
                int indvdlCaravSiteCo = item.optInt("indvdlCaravSiteCo", 0);
                int roomCount = gnrlSiteCo + autoSiteCo + glampSiteCo + caravSiteCo + indvdlCaravSiteCo;
                
                String firstImageUrl = item.optString("firstImageUrl", "");
                if (!firstImageUrl.isEmpty()) {
                  
                CampingItem campingItem = new CampingItem(
                        item.optString("facltNm", "N/A"),
                        item.optString("contentId", "N/A"),
                        item.optString("manageSttus", "N/A"),
                        item.optString("induty", "N/A"),
                        item.optString("lctCl", "N/A"),
                        item.optString("doNm", "N/A"),
                        item.optString("sigunguNm", "N/A"),
                        item.optString("operPdCl", "N/A"),
                        item.optString("sbrsCl", "N/A"),
                        item.optString("facltDivNm", "N/A"),
                        item.optString("siteBottomCl1", "N/A"),
                        item.optString("siteBottomCl2", "N/A"),
                        item.optString("siteBottomCl3", "N/A"),
                        item.optString("siteBottomCl4", "N/A"),
                        item.optString("siteBottomCl5", "N/A"),
                        item.optString("intro", "N/A"),
                        item.optString("firstImageUrl", "N/A"),
                        item.optString("addr1", "N/A"),
                        item.optString("tel", "N/A"),
                        item.optString("homepage", "N/A"),
                        item.optString("resveUrl", "N/A"),
                        item.optString("resveCl", "N/A"),
                        item.optString("themaEnvrnCl", "N/A"),
                        item.optString("eqpmnLendCl", "N/A"),
                        Integer.toString(roomCount) // Converting roomCount to String
                        
                );
                // 이미지 처리
                
                items.add(campingItem);
            }
            }
            return new CampingApiResponse(totalCount, items);
        } else {
            throw new RuntimeException("API request failed with response code: " + responseCode);
        }
    }

    public List<CampingItem> getAllCampingSites() throws Exception {
        List<CampingItem> allItems = new ArrayList<>();
        int page = 1;
        int pageSize = 1000; // 최대 크기로 설정하여 여러 번 호출 필요 줄이기
        int totalCount;

        do {
            CampingApiResponse response = getCampingSites(page, pageSize);
            totalCount = response.getTotalCount();
            allItems.addAll(response.getItems());
            page++;
        } while (allItems.size() < totalCount);

        return allItems;
    }

    public List<String> getImageList(int contentId) throws Exception {
        String urlStr = "http://apis.data.go.kr/B551011/GoCamping/imageList" +
                "?serviceKey=" + SERVICE_KEY +
                "&MobileOS=AND" +
                "&MobileApp=appName" +
                "&_type=json" +
                "&contentId=" + contentId;
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        int responseCode = conn.getResponseCode();
        System.out.println("Response Code : " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { // 200
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Parse JSON response
            JSONObject jsonObj = new JSONObject(response.toString());
            
            JSONObject responseBody = jsonObj.getJSONObject("response").getJSONObject("body");

            // items에서 필요한 정보 추출
            List<String> imageUrls = new ArrayList<>();
            JSONObject itemsObject = responseBody.getJSONObject("items"); // JSONObject로 변경
            JSONArray jsonItems = itemsObject.getJSONArray("item");
            for (int i = 0; i < jsonItems.length(); i++) {
                JSONObject item = jsonItems.getJSONObject(i);
                imageUrls.add(item.optString("imageUrl", "N/A"));
            }

            return imageUrls;
        } else {
            throw new RuntimeException("API request failed with response code: " + responseCode);
        }
}
    }
