package com.demo.persistence;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.demo.domain.Review;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

	// 전체 리뷰 조회
    @Query(value = "SELECT * FROM review ORDER BY review_seq DESC", nativeQuery = true)
    List<Review> getAllReview();

    // 리뷰 상세 조회
    @Query(value = "SELECT * FROM review WHERE review_seq = :review_seq", nativeQuery = true)
    Review getReviewBySeq(@Param("review_seq") int review_seq);

    // ID로 리뷰 조회
    @Query("SELECT r FROM Review r WHERE r.member.id = :id ORDER BY r.review_seq DESC")
    List<Review> getByIdReview(@Param("id") String id);

    // 제목으로 리뷰 조회
    @Query("SELECT r FROM Review r WHERE r.title LIKE %:title% ORDER BY r.review_seq DESC")
    List<Review> getByTitleReview(@Param("title") String title);

    // 즐겨찾기순 정렬
    @Query(value = "SELECT * FROM review ORDER BY checkCount DESC", nativeQuery = true)
    List<Review> getReviewsSortedByCheckCount();

    // 추천순 정렬
    @Query(value = "SELECT * FROM review ORDER BY recoCount DESC", nativeQuery = true)
    List<Review> getReviewsSortedByRecoCount();

    // 조회순 정렬
    @Query(value = "SELECT * FROM review ORDER BY viewCount DESC", nativeQuery = true)
    List<Review> getReviewsSortedByViewCount();
    
    // 마이페이지용
    // 내가 작성한 글 조회
    @Query("SELECT r FROM Review r WHERE r.member.id =:id ORDER BY r.review_seq DESC")
    public List<Review> getMyReview(@Param("id") String id);
    // 내가 즐겨찾기한 게시글
    @Query("SELECT f.review FROM Favorite f WHERE f.member.id = :memberId")
    List<Review> findFavoriteReviewsByMemberId(@Param("memberId") String id);
}
