package com.demo.service;

import java.util.List;

import com.demo.domain.Reply;

public interface ReplyService {

	// 특정 게시물 리뷰 조회
	List<Reply> getReplyByReview(int review_seq);
	
	// 특정 회원 리뷰 조회
	List<Reply> getReplyByMember(String id);
	
	// 댓글 좋아요
	boolean toggleLike(String userId, int replySeq) throws Exception;
	
	// 댓글 저장
	void saveReply(Reply vo);
	
	// 댓글 수정
	void updateReply(int replySeq, Reply vo);
	
	// 댓글 삭제
	void deleteReply(int replySeq);
	
	Reply getReplyBySeq(int replySeq);
}
