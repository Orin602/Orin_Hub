package com.demo.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.domain.FileUploadUtil;
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


	// 이미지 삭제
	@Override
	@Transactional
	public void deleteImage(int review_seq, int imageIndex) {
	    Review review = reviewRepo.getReviewBySeq(review_seq);
	    List<String> uploadImages = review.getUploadedImages();
	    
	    if (imageIndex >= 0 && imageIndex < uploadImages.size()) {
	        // 이미지 리스트에서 해당 인덱스의 이미지 삭제
	        String imageUrlToRemove = uploadImages.remove(imageIndex);
	        
	        try {
	            // 파일 시스템에서 이미지 삭제
	            FileUploadUtil.deleteFile(imageUrlToRemove);
	        } catch (IOException e) {
	            // IOException을 RuntimeException으로 전환하여 처리
	            throw new RuntimeException("Failed to delete image file: " + imageUrlToRemove, e);
	        }
	        
	        // 변경된 이미지 리스트를 Review 객체에 설정
	        review.setUploadedImages(uploadImages);
	        reviewRepo.save(review);
	    } else {
	        throw new IllegalArgumentException("Invalid image index: " + imageIndex);
	    }
	}

	@Override
    @Transactional
    public void incrementViewCount(int reviewSeq) throws Exception {
        reviewRepo.incrementViewCount(reviewSeq);
    }

    @Override
    @Transactional
    public void incrementRecoCount(int reviewSeq) throws Exception {
        reviewRepo.incrementRecoCount(reviewSeq);
    }

    @Override
    @Transactional
    public void incrementCheckCount(int reviewSeq) throws Exception {
        reviewRepo.incrementCheckCount(reviewSeq);
    }

    @Override
    @Transactional
    public void decrementRecoCount(int reviewSeq) throws Exception {
        reviewRepo.decrementRecoCount(reviewSeq);
    }

    @Override
    @Transactional
    public void decrementCheckCount(int reviewSeq) throws Exception {
        reviewRepo.decrementCheckCount(reviewSeq);
    }

}
