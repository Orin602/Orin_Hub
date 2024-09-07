package com.demo.persistence;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.demo.domain.MemberData;
import com.demo.domain.Review;

public interface ReviewRepository extends JpaRepository<Review, Integer> {	

    // 해당 리뷰 출력
    @Query(value="SELECT * FROM Review r WHERE r.review_seq = ?1 ", nativeQuery=true)
    public Review getReview(int review_seq); 
    
    // 전체 리뷰 리스트 출력
    @Query(value="SELECT * FROM Review ORDER BY review_seq DESC ", nativeQuery=true)
    public List<Review> getReviewList();
    
    @Query(value="SELECT * FROM Review WHERE kakao_id = ?1 ", nativeQuery=true)
    public List<Review> findByKakaoId(String kakao_id);
    
    // 카카오 아이디로 검색
    @Query(value="SELECT * FROM Review WHERE kakao_id = ?1 ", nativeQuery=true)
    public Page<Review> findReviewByKakao_idOrderByKakao_id(String kakao_id, int review_seq, Pageable pageable);
    
    // 제목으로 검색
    @Query(value="SELECT * FROM Review WHERE title LIKE %?1% ", nativeQuery=true)
    public Page<Review> findReviewByTitleContainingOrderByTitle(String title, int review_seq, Pageable pageable);
    
    // 글쓴이 아이디로 검색
    @Query(value="SELECT r.id , r.review_seq , r.kakao_id, r.kakao_name, r.title, r.content, r.write_date , r.cnt, r.goodpoint, r.bookmark FROM Review r JOIN Member_Data m ON r.id = m.no_data WHERE m.id = ?1", nativeQuery=true)
    public Page<Review> findReviewByIdContainingOrderById(String id, int review_seq, Pageable pageable);
    
    // 말머리 검색(캠핑장)
    @Query(value="SELECT * FROM Review WHERE kakao_name LIKE %?1% ", nativeQuery=true)
    public Page<Review> findReviewBykakao_nameContainingOrderBykakao_name(String kakao_name, int review_seq, Pageable pageable);
    
    // 전체리뷰 페이징처리 
    @Query(value="SELECT * FROM Review ORDER BY review_seq DESC ", nativeQuery=true)
    public Page<Review> findAllReview(int review_seq, Pageable pageable); // 전체글 페이징처리 
    
    // 조회순 정렬
    @Query(value="SELECT * FROM Review ORDER BY cnt DESC ", nativeQuery=true)
    public Page<Review> findAllByOrderByCntDesc(int review_seq, Pageable pageable); 
    
    // 추천순 정렬
    @Query(value="SELECT * FROM Review ORDER BY goodpoint DESC ", nativeQuery=true)
    public Page<Review> findAllByOrderByGoodpointDesc(int review_seq, Pageable pageable);
    
    // 북마크순 정렬
    @Query(value="SELECT * FROM Review ORDER BY bookmark DESC ", nativeQuery=true)
    public Page<Review> findAllByOrderByBookmarkDesc(int review_seq, Pageable pageable);
    
 // 사용자 ID로 리뷰 가져오기
    @Query(value="SELECT r FROM Review r WHERE r.member_data.id = :id")
    List<Review> getReviewsById(@Param("id") String id);
    
 // 사용자 ID로 사용자가 북마크한 리뷰와 해당 리뷰를 작성한 다른 사람이 작성한 글 중에서 북마크한 리뷰를 가져오는 쿼리
    @Query(value="SELECT r FROM Review r WHERE r.bookmark = 1")
    List<Review> findBookmarkedReviewsById(@Param("id") String id);


}







