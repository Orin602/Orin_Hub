package com.demo.persistence;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.demo.domain.ReviewReply;

public interface ReviewReplyRepository extends JpaRepository<ReviewReply, Integer> {
	
		//댓글 페이징
		@Query(value="SELECT * FROM Review_Reply r WHERE r.review.review_seq = ?1 ", nativeQuery=true)
		Page<ReviewReply> findReviewReplyByreplynumContainingOrderByReplynum(int review_seq, Pageable pageable);
		
		// 댓글 출력
		@Query(value="SELECT r.replynum, r.content, r.review_seq, r.no_data, r.r_regdate FROM Review_Reply r JOIN Review w ON (r.review_seq = w.review_seq) WHERE w.review_seq = ?1 ORDER BY r.r_regdate DESC ", nativeQuery=true)
		List<ReviewReply> getReviewReplyList(int review_seq);
		
		//해당댓글만
		@Query(value="SELECT r.* FROM Review_Reply r WHERE r.replynum = ?1 ", nativeQuery=true)
		public ReviewReply getReviewReplyByReplynum(int replynum);
}