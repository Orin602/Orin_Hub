package com.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.demo.domain.Review;
import com.demo.persistence.MemberRepository;
import com.demo.persistence.ReviewRepository;

@Service
public class ReviewServiceImpl implements ReviewService {
	
	@Autowired
	ReviewRepository reviewRepo;
	MemberRepository memberRepo;

	//리뷰 작성
	@Override
	public void insertReview(Review vo) {
		 reviewRepo.save(vo);
	}
	
	//리뷰수정
	@Override
	public void updateReview(Review vo) {
		Review p = reviewRepo.getReview(vo.getReview_seq());
		vo.setWrite_date(p.getWrite_date());
		vo.setReview_seq(p.getReview_seq());
		reviewRepo.save(vo);

	}
	
	//리뷰삭제
	@Override
	public void deleteReview(Review vo) {
		reviewRepo.delete(vo);

	}
	
	//상세정보 (클릭시)
	@Override
	public Review getReview(int review_seq) {
		return reviewRepo.getReview(review_seq);
	}
	
	//리뷰 리스트
	@Override
	public List<Review> getReview() {
		return reviewRepo.getReviewList();
	}

	//리뷰 페이징
	@Override
	public Page<Review> getAllReview(int review_seq, int page, int size) {
		Pageable pageable = PageRequest.of(page - 1, size, Direction.ASC, "review_seq");
		return reviewRepo.findAllReview(review_seq, pageable);
	}
	
	//제목으로 검색
	@Override
	public Page<Review> getReviewByTitle(int review_seq, int page, int size, String title) {
		Pageable pageable = PageRequest.of(page - 1, size, Direction.ASC, "review_seq");
		return reviewRepo.findReviewByTitleContainingOrderByTitle(title, review_seq, pageable);
	}
	
	// 글쓴이로 검색
	@Override
	public Page<Review> getReviewByWriter(int review_seq, int page, int size, String id) {
		Pageable pageable = PageRequest.of(page - 1, size, Direction.ASC, "review_seq");
		return reviewRepo.findReviewByIdContainingOrderById(id, review_seq, pageable);
	}
	
	// 캠핑장(말머리) 검색
	@Override
	public Page<Review> getReviewByCamping(int review_seq, int page, int size, String kakao_name) {
		Pageable pageable = PageRequest.of(page - 1, size, Direction.ASC, "review_seq");
		return reviewRepo.findReviewBykakao_nameContainingOrderBykakao_name(kakao_name, review_seq, pageable);
	}
	
	//조회수 정렬
	@Override
	public Page<Review> getCReviewByCnt(int review_seq, int page, int size) {
		Pageable pageable = PageRequest.of(page - 1, size, Direction.ASC, "review_seq");
		return reviewRepo.findAllByOrderByCntDesc(review_seq, pageable);
	}
	
	//추천순 정렬
	@Override
	public Page<Review> getReviewByGoodpoint(int review_seq, int page, int size) {
		Pageable pageable = PageRequest.of(page - 1, size, Direction.ASC, "review_seq");
		return reviewRepo.findAllByOrderByGoodpointDesc(review_seq, pageable);
	}
	
	//북마크순 정렬
	@Override
	public Page<Review> getReviewByBookmark(int bookmark, int page, int size) {
		Pageable pageable = PageRequest.of(page - 1, size, Direction.ASC, "review_seq");
		return reviewRepo.findAllByOrderByBookmarkDesc(bookmark, pageable);
	}
	
	// 캠핑장별 리뷰
	public List<Review> getReviewsByKakaoId(String kakao_id) {
        return reviewRepo.findByKakaoId(kakao_id);
    }
	
	@Override
	public Page<Review> getReviewBykakao_id(int review_seq, int page, int size, String kakao_id) {
		Pageable pageable = PageRequest.of(page - 1, size, Direction.ASC, "review_seq");
		return reviewRepo.findReviewByKakao_idOrderByKakao_id(kakao_id, review_seq, pageable);
	}

	// 사용자 ID를 기반으로 리뷰 가져오기
    @Override
    public List<Review> getReviewsById(String id) {
        return reviewRepo.getReviewsById(id);
    }

    @Override
    public Review getReviewById(int reviewId) {
        Optional<Review> optionalReview = reviewRepo.findById(reviewId);
        return optionalReview.orElse(null);
    }

	@Override
	public List<Review> getBookmarkedReviews(String id) {
		return reviewRepo.findBookmarkedReviewsById(id);
	}


}
