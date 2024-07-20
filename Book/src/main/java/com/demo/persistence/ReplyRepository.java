package com.demo.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import com.demo.domain.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Integer> {

	// 특정 리뷰에 대한 모든 댓글 조회
	@Query("SELECT r FROM Reply r WHERE r.review.review_seq =:review_seq ORDER BY reply_date")
    List<Reply> findByReview(@RequestParam("review_seq") int review_seq);

    // 특정 사용자가 작성한 모든 댓글 조회
	@Query("SELECT r FROM Reply r WHERE r.member.id =:id")
    List<Reply> findByMember(@RequestParam("id") String id);
    
    // 댓글 좋아요 수 증가
    @Modifying
    @Transactional
    @Query("UPDATE Reply r SET r.likes = r.likes + 1 WHERE r.replySeq = :replySeq")
    void incrementLikes(int replySeq);
    
}
