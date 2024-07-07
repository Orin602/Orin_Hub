package com.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.demo.domain.Review;
import com.demo.persistence.ReviewRepository;

@Service
public class ReveiwServiceImpl implements ReviewService {
	
	@Autowired
	private ReviewRepository reviewRepo;

	// 리뷰 작성
	@Override
	public void insertReview(Review vo) {
		reviewRepo.save(vo);

	}

	// 리뷰 수정
	@Override
	public void updateReview(Review vo) {
		Review update_review = reviewRepo.getReview(vo.getReview_seq());
		vo.setWrite_date(update_review.getWrite_date());	// 작성 시간 유지
		vo.setReview_seq(update_review.getReview_seq());	// 게시글 고유 번호 유지
		vo.setMember(update_review.getMember());	// 작성자 유지
		vo.setImagePath(update_review.getImagePath());
		reviewRepo.save(vo);
		
	}

	// 리뷰 삭제
	@Override
	public void deleteReview(Review vo) {
		reviewRepo.delete(vo);
		
	}

	// 리뷰 상세
	@Override
	public Review getReview(int review_seq) {
		
		return reviewRepo.getReview(review_seq);
	}

	// 전체 리뷰 페이징
	@Override
	public Page<Review> getAllReview(int page, int size) {
		/*
		 * page-1 : Spring Data JPA에서는 페이지 번호가 0부터 시작
		 * size : 페이지당 항목 수
		 * Direction : 정렬 방향을 설정 (ASC=오름차순, DESC=내림차순)
		 * review : 정렬할 컬럼명을 설정
		 */
		Pageable pageable = PageRequest.of(page -1, size, Direction.ASC, "review_seq");
		return reviewRepo.findAllReview(pageable);
	}

	// 제목 검색
	@Override
	public Page<Review> getReviewByTitle(int page, int size, String title) {
		Pageable pageable = PageRequest.of(page -1, size, Direction.ASC, "review_seq");
		return reviewRepo.findReviewByTitle(title, pageable);
	}

	// ID 검색
	@Override
	public Page<Review> getReviewById(int page, int size, String id) {
		Pageable pageable = PageRequest.of(page -1, size, Direction.ASC, "review_seq");
		return reviewRepo.findReviewById(id, pageable);
	}

	// 즐겨찾기순 정렬
	@Override
	public Page<Review> getReviewByFavoriteCount(int page, int size) {
		Pageable pageable = PageRequest.of(page -1, size, Direction.ASC, "review_seq");
		return reviewRepo.findAllOrderByFavoriteCountDesc(pageable);
	}

	// 조회순 정렬
	@Override
	public Page<Review> getReviewByViewCount(int page, int size) {
		Pageable pageable = PageRequest.of(page -1, size, Direction.ASC, "review_seq");
		return reviewRepo.findAllOrderByViewCountDesc(pageable);
	}

	// 추천순 정렬
	@Override
	public Page<Review> getReviewByRecommendCount(int page, int size) {
		Pageable pageable = PageRequest.of(page -1, size, Direction.ASC, "review_seq");
		return reviewRepo.findAllOrderByRecommendCountDesc(pageable);
	}

	// 내가 작성한 리뷰 목록
	@Override
	public List<Review> getMyReview(String id) {

		return reviewRepo.getMyReview(id);
	}

	@Override
	public List<Review> getMyFavoriteReviews(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Review> getAllReviewList() {
		
		return reviewRepo.getAllReviewList();
	}

	// 내가 즐겨찾기한 리뷰 목록
//	@Override
//	public List<Review> getMyFavoriteReviews(String id) {
//		
//		return reviewRepo.findMyFavoriteReviews(id);
//	}

}
