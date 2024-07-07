package com.demo.persistence;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.demo.domain.Review;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

	// 전체 게시글 조회
	@Query(value="SELECT * FROM review ORDER BY review_seq DESC", nativeQuery=true)
	public List<Review> getAllReviewList();
	
	// 전체리뷰 페이징처리 
	@Query(value="SELECT * FROM review ORDER BY review_seq DESC", countQuery="SELECT COUNT(*) FROM review", nativeQuery=true)
	public Page<Review> findAllReview(Pageable pageable);

	
	// 특정 게시글 조회
	@Query(value="SELECT * FROM review r WHERE r.review_seq = ?1", nativeQuery=true)
	public Review getReview(int review_seq);
	
	// 제목 검색 게시글 조회
	@Query(value="SELECT * FROM review WHERE title LIKE %?1% ORDER BY review_seq DESC", countQuery="SELECT COUNT(*) FROM review WHERE title LIKE %?1%", nativeQuery=true)
	public Page<Review> findReviewByTitle(String title, Pageable pageable);
	
	// ID 검색 게시글 조회
	@Query(value="SELECT * FROM review WHERE id = ?1 ORDER BY review_seq DESC", countQuery="SELECT COUNT(*) FROM review WHERE id = ?1", nativeQuery=true)
	public Page<Review> findReviewById(String id, Pageable pageable);
	
	// 즐겨찾기순 정렬
	@Query(value = "SELECT r FROM Review r ORDER BY r.favoriteCount DESC", countQuery = "SELECT COUNT(r) FROM Review r")
	Page<Review> findAllOrderByFavoriteCountDesc(Pageable pageable);
	
	// 추천순 정렬
	@Query(value = "SELECT r FROM Review r ORDER BY r.recommendCount DESC", countQuery = "SELECT COUNT(r) FROM Review r")
	Page<Review> findAllOrderByRecommendCountDesc(Pageable pageable);

	// 조회순 정렬
	@Query(value = "SELECT r FROM Review r ORDER BY r.viewCount DESC", countQuery = "SELECT COUNT(r) FROM Review r")
	Page<Review> findAllOrderByViewCountDesc(Pageable pageable);

	
	/* 
	 * 마이페이지용
	*/
	// 내가 작성한 게시글
	@Query("SELECT r FROM Review r WHERE r.member.id = :id ORDER BY r.review_seq DESC")
	public List<Review> getMyReview(@Param("id") String id);
	
	// 내가 즐겨찾기한 게시글
//    @Query("SELECT r FROM Review r WHERE :id MEMBER OF r.favoriteMembers ORDER BY r.review_seq DESC")
//    List<Review> findMyFavoriteReviews(@Param("id") String id);
}
