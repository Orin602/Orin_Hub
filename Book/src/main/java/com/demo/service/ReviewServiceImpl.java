package com.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.demo.domain.Review;
import com.demo.persistence.ReviewRepository;

@Service
public class ReviewServiceImpl implements ReviewService {

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
		// 특정 리뷰 조회
		Review update_review = reviewRepo.getReviewBySeq(vo.getReview_seq());
		vo.setReview_seq(update_review.getReview_seq());	// 게시글 고유 번호 유지
		vo.setReview_date(update_review.getReview_date());	// 작성 시간 유지
		vo.setMember(update_review.getMember());	// 작성자 유지
		
		reviewRepo.save(vo);
	}

	// 리뷰 삭제
	@Override
	public void deleteReview(Review vo) {
		reviewRepo.delete(vo);

	}

	// 전체 리뷰 조회
	@Override
	public List<Review> getAllReview() {
		
		return reviewRepo.findAll();
	}

	// 리뷰 상세 조회
	@Override
	public Review getReviewBySeq(int review_seq) {
		
		return reviewRepo.getReviewBySeq(review_seq);
	}

	// ID검색 리뷰 조회
	@Override
	public List<Review> getByIdReview(String id) {
		
		return reviewRepo.getByIdReview(id);
	}

	// 제목검색 리뷰 조회
	@Override
	public List<Review> getByTitleReview(String title) {
		
		return reviewRepo.getByTitleReview(title);
	}

	// 즐겨찾기순 정렬
	@Override
	public List<Review> getReviewsSortedByCheckCount() {
		
		return reviewRepo.getReviewsSortedByCheckCount();
	}

	// 추천순 정렬
	@Override
	public List<Review> getReviewsSortedByRecoCount() {
		
		return reviewRepo.getReviewsSortedByRecoCount();
	}

	// 조회순 정렬
	@Override
	public List<Review> getReviewsSortedByViewCount() {
		
		return reviewRepo.getReviewsSortedByViewCount();
	}

	// 내가 작성한 게시글 조회
	@Override
	public List<Review> getMyReview(String id) {
		
		return reviewRepo.getMyReview(id);
	}

	// 내가 즐겨찾기한 게시글 조회
	@Override
	public List<Review> findFavoriteReviewsByMemberId(String id) {
		
		return reviewRepo.findFavoriteReviewsByMemberId(id);
	}

}
