package com.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true) // 알 수 없는 속성을 무시
public class ItemDTO {
    private String title;       // 책 제목
    private String author;      // 저자
    private String description; // 책 설명 (필요시 추가)
    private String cover;       // 책 표지 이미지 URL
    private String link;        // 도서 상세 페이지 링크
    
    // 국립중앙도서관 api용
    private String kwd;          // 검색어
    private String titleInfo;   // 제목
    private String authorInfo;  // 저자
    private String imageUrl;    // 이미지 URL
}
