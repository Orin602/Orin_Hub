package com.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.domain.Reply;
import com.demo.persistence.ReplyRepository;

@Service
public class ReplyServiceImpl implements ReplyService {

	@Autowired
	private ReplyRepository replyRepo;
	
	// 특정 게시글 댓글
	@Override
	public List<Reply> getReplyByReview(int review_seq) {
		
		return replyRepo.findByReview(review_seq);
	}

	// 특정 회원 댓글
	@Override
	public List<Reply> getReplyByMember(String id) {
		
		return replyRepo.findByMember(id);
	}

	// 댓글 좋아요 수 증가
	@Override
	@Transactional
	public void incrementLike(int replySeq) {
		replyRepo.incrementLikes(replySeq);

	}

	// 댓글 저장
	@Override
	public void saveReply(Reply vo) {
		replyRepo.save(vo);

	}

	// 댓글 수정
	@Override
	@Transactional
	public void updateReply(int replySeq, Reply vo) {
		Optional<Reply> beforeReply = replyRepo.findById(replySeq); 
		if(beforeReply.isPresent()) {
			Reply reply = beforeReply.get();
			reply.setContent(vo.getContent());
			replyRepo.save(reply);
		} else {
			throw new IllegalArgumentException("잘못된 댓글 번호: " + replySeq);
		}
	}

	// 댓글 삭제
	@Override
	public void deleteReply(int replySeq) {
		replyRepo.deleteById(replySeq);

	}

}
