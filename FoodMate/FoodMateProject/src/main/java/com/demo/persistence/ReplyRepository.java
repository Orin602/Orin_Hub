package com.demo.persistence;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.demo.domain.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Integer> {
	
		//댓글 페이징
		@Query(value="SELECT * FROM Reply r WHERE r.com_board_detail.seq = ?1 ", nativeQuery=true)
		Page<Reply> findReplyByreplynumContainingOrderByReplynum(int replynum, Pageable pageable);
		
		// 댓글 출력
		@Query(value="SELECT r.* FROM Reply r JOIN Com_Board_Detail cbd ON (r.seq = cbd.seq) WHERE cbd.seq = ?1 ORDER BY r_regdate DESC ", nativeQuery=true)
		List<Reply> getReplyList(int seq);
		
		//해당댓글만
		@Query(value="SELECT r.* FROM Reply r WHERE r.replynum = ?1 ", nativeQuery=true)
		public Reply getReplyByReplynum(int replynum);
}