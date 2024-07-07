package com.demo.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.demo.domain.Review;

public interface ReviewService {

	void insertReview(Review vo);    // 게시글 작성
    void updateReview(Review vo);    // 게시글 수정
    void deleteReview(Review vo);    // 게시글 삭제

    // 특정 게시글 조회
    Review getReview(int review_seq);

    List<Review> getAllReviewList();
    // 전체 게시글 조회 (페이징)
    Page<Review> getAllReview(int page, int size);

    // 제목 검색 게시글 조회 (페이징)
    Page<Review> getReviewByTitle(int page, int size, String title);

    // ID 검색 게시글 조회 (페이징)
    Page<Review> getReviewById(int page, int size, String id);

    // 즐겨찾기순 정렬 게시글 조회 (페이징)
    Page<Review> getReviewByFavoriteCount(int page, int size);

    // 조회순 정렬 게시글 조회 (페이징)
    Page<Review> getReviewByViewCount(int page, int size);

    // 추천순 정렬 게시글 조회 (페이징)
    Page<Review> getReviewByRecommendCount(int page, int size);

    // 내가 작성한 게시글 조회
    List<Review> getMyReview(String id);

    // 내가 즐겨찾기한 게시글 조회
    List<Review> getMyFavoriteReviews(String id);
}
