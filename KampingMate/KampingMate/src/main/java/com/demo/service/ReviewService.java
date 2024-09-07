package com.demo.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.demo.domain.MemberData;
import com.demo.domain.Review;

public interface ReviewService {
	
	
	public void insertReview(Review vo);
	
	public void updateReview(Review vo);
	
	public void deleteReview(Review vo);

	public Review getReview(int review_seq); //seq로 해당글 찾기
	
	public List<Review> getReview();
	
	public Page<Review> getAllReview(int review_seq, int page, int size); //전체 페이징
	
	public Page<Review> getReviewBykakao_id(int review_seq, int page, int size, String title);//카카오아이디로
	
	public List<Review> getReviewsByKakaoId(String kakao_id);
	
	public Page<Review> getReviewByTitle(int review_seq, int page, int size, String title); // 제목으로검색
	
	public Page<Review> getReviewByWriter(int review_seq, int page, int size, String id); //작성자로 검색
	
	public Page<Review> getReviewByCamping(int review_seq, int page, int size, String kakao_name); //말머리(캠핑장명)으로 검색
	
	public Page<Review> getCReviewByCnt(int review_seq, int page, int size); // 조회순 정렬
	
	public Page<Review> getReviewByGoodpoint(int review_seq, int page, int size); // 추천순 정렬
	
	public Page<Review> getReviewByBookmark(int review_seq, int page, int size); //북마크순 정렬
	
	public List<Review> getReviewsById(String id);

	public Review getReviewById(int reviewId);

	public List<Review> getBookmarkedReviews(String id);

}
