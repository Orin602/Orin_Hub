package com.demo.service;

import java.util.HashMap;
import java.util.Map;

public class RegionMapping {
    public static final Map<String, String> DO_NAME_MAPPING;
    static {
        DO_NAME_MAPPING = new HashMap<>();
        DO_NAME_MAPPING.put("서울시", "서울특별시");
        DO_NAME_MAPPING.put("부산시", "부산광역시");
        DO_NAME_MAPPING.put("대구시", "대구광역시");
        DO_NAME_MAPPING.put("인천시", "인천광역시");
        DO_NAME_MAPPING.put("광주시", "광주광역시");
        DO_NAME_MAPPING.put("대전시", "대전광역시");
        DO_NAME_MAPPING.put("울산시", "울산광역시");
        DO_NAME_MAPPING.put("세종시", "세종특별자치시");
        DO_NAME_MAPPING.put("경기도", "경기도");
        DO_NAME_MAPPING.put("강원도", "강원도");
        DO_NAME_MAPPING.put("충청북도", "충청북도");
        DO_NAME_MAPPING.put("충청남도", "충청남도");
        DO_NAME_MAPPING.put("전라북도", "전라북도");
        DO_NAME_MAPPING.put("전라남도", "전라남도");
        DO_NAME_MAPPING.put("경상북도", "경상북도");
        DO_NAME_MAPPING.put("경상남도", "경상남도");
        DO_NAME_MAPPING.put("제주도", "제주특별자치도");
        
        DO_NAME_MAPPING.put("서울특별시", "서울시");
        DO_NAME_MAPPING.put("부산광역시", "부산시");
        DO_NAME_MAPPING.put("대구광역시", "대구시");
        DO_NAME_MAPPING.put("인천광역시", "인천시");
        DO_NAME_MAPPING.put("광주광역시", "광주시");
        DO_NAME_MAPPING.put("대전광역시", "대전시");
        DO_NAME_MAPPING.put("울산광역시", "울산시");
        DO_NAME_MAPPING.put("세종특별자치시", "세종시");
        DO_NAME_MAPPING.put("경기도", "경기도");
        DO_NAME_MAPPING.put("강원도", "강원도");
        DO_NAME_MAPPING.put("충청북도", "충청북도");
        DO_NAME_MAPPING.put("충청남도", "충청남도");
        DO_NAME_MAPPING.put("전라북도", "전라북도");
        DO_NAME_MAPPING.put("전라남도", "전라남도");
        DO_NAME_MAPPING.put("경상북도", "경상북도");
        DO_NAME_MAPPING.put("경상남도", "경상남도");
        DO_NAME_MAPPING.put("제주특별자치도", "제주도");
    }

    public static String mapDoName(String doName) {
        return DO_NAME_MAPPING.getOrDefault(doName, doName);
    }
}
