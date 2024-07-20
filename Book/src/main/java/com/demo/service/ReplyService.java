package com.demo.service;

import java.util.List;

import com.demo.domain.Reply;

public interface ReplyService {

	// 특정 게시물 리뷰 조회
	List<Reply> getReplyByReview(int review_seq);
	
	// 특정 회원 리뷰 조회
	List<Reply> getReplyByMember(String id);
	
	// 댓글 좋아요
	void incrementLike(int replySeq);
	
	void saveReply(Reply vo);
	
	void updateReply(int replySeq, Reply vo);
	
	void deleteReply(int replySeq);
	
}
