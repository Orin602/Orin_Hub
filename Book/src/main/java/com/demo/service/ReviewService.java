package com.demo.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.demo.domain.Review;

public interface ReviewService {

	// 리뷰 작성
	void insertReview(Review vo);
	// 리뷰 수정
	void updateReview(Review vo);
	// 리뷰 삭제
	void deleteReview(Review vo);
	
	// 전체 리뷰 조회
	List<Review> getAllReview();
	
	// 리뷰 상세 조회
	Review getReviewBySeq(int review_seq);
	
	// ID검색 리뷰 조회
	List<Review> getByIdReview(String id);
	
	// 제목검색 리뷰 조회
	List<Review> getByTitleReview(String title);
	
	// 즐겨찾기순 정렬
	List<Review> getReviewsSortedByCheckCount();

    // 추천순 정렬
	List<Review> getReviewsSortedByRecoCount();

    // 조회순 정렬
	List<Review> getReviewsSortedByViewCount();
    
	void incrementViewCount(int reviewSeq) throws Exception;	// 조회수 +
    void incrementRecoCount(int reviewSeq) throws Exception;	// 추천수 +
    void incrementCheckCount(int reviewSeq) throws Exception;	// 즐겨찾기수 +
    void decrementRecoCount(int reviewSeq) throws Exception;	// 추천수 -
    void decrementCheckCount(int reviewSeq) throws Exception;	// 즐겨찾기수 -
    
    // 마이페이지용
    // 내가 작성한 글 조회
    List<Review> getMyReview(String id);
    
    // 이미지 삭제
	void deleteImage(int review_seq, int imageIndex);
	
}
