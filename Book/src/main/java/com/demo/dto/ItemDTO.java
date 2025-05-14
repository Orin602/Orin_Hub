package com.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true) // 알 수 없는 속성을 무시
public class ItemDTO {
	// 알라딘 api용(도서구매)
    private String title;       	// 책 제목
    private String author;      	// 저자
    private String description; 	// 책 설명 (필요시 추가)
    private String cover;       	// 책 표지 이미지 URL
    private String link;        	// 도서 상세 페이지 링크
    private int pricesales;		// 판매가
    private int pricestandard;	// 정가
    private int customerReviewRank; // 회원 리뷰 평점(별점 평균) : 0~10점(별0.5개당 1점)
    
    // 추가 : 알라딘 api 응답 결과
    private int totalResults;	// API의 총 결과 수
    private int startIndex;		// 결과 시작 페이지
    private int itemsPerPage;	// 한 페이지에 출력되는 상품 수
    private String query;		// 요청 검색어
    
    // 국립중앙도서관 api용
    private String kwd;          // 검색어
    private String titleInfo;   // 제목
    private String authorInfo;  // 저자
    private String imageUrl;    // 이미지 URL
    private String kdcName1s;
    
	// 도서 식별용
	private String docId; // 국립중앙도서관 도서 ID
}
