package com.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.demo.domain.ReviewReply;
import com.demo.persistence.ReviewReplyRepository;

@Service
public class ReviewReplyServiceImpl implements ReviewReplyService {
	
	@Autowired
	ReviewReplyRepository ReplyRepo;

	@Override
	public void insertReply(ReviewReply vo) {
		ReplyRepo.save(vo);

	}

	@Override
	public void updateReply(ReviewReply vo) {
		ReviewReply p = ReplyRepo.findById(vo.getReplynum()).get();
		vo.setR_regdate(p.getR_regdate());
		ReplyRepo.save(vo);

	}

	@Override
	public void deleteReply(ReviewReply vo) {
		ReplyRepo.delete(vo);

	}

	@Override
	public List<ReviewReply> getReplyBySeq(int review_seq) {
		return ReplyRepo.getReviewReplyList(review_seq);
	}

	@Override
	public Page<ReviewReply> getReplyList_paging(int replynum, int page, int size) {
		Pageable pageable = PageRequest.of(page-1, size, Direction.DESC, "replynum");
		return ReplyRepo.findReviewReplyByreplynumContainingOrderByReplynum(replynum, pageable);
	}

	@Override
	public ReviewReply findReplyByreplynum(int replynum) {
		return ReplyRepo.getReviewReplyByReplynum(replynum);
	}

//	@Override
//	public List<Review> getMyRecipe(String id) {
//		return BoardDetailRepo.getMyRecipeListById(id);
//	}

}
