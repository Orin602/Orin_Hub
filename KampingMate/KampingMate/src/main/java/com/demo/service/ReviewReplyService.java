package com.demo.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.demo.domain.ReviewReply;

public interface ReviewReplyService {
	
	public void insertReply(ReviewReply vo);
	
	public void updateReply(ReviewReply vo);
	
	public void deleteReply(ReviewReply vo);
	
	public List<ReviewReply> getReplyBySeq(int seq);
	
	public Page<ReviewReply> getReplyList_paging(int replynum , int page, int size);
	
	public ReviewReply findReplyByreplynum(int replynum);
	
	// 회원별 작성한 레시피 목록(마이페이지용)
//    public List<Review> getMyRecipe(String id);


}
