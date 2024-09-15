package com.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.domain.Member;
import com.demo.domain.Reply;
import com.demo.domain.ReplyLike;
import com.demo.persistence.ReplyLikeRepository;
import com.demo.persistence.ReplyRepository;

@Service
public class ReplyServiceImpl implements ReplyService {

	@Autowired
	private ReplyRepository replyRepo;
	@Autowired
	private ReplyLikeRepository replyLikeRepo;
	
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
	public boolean toggleLike(String userId, int replySeq) throws Exception {
		boolean alreadyLiked = replyLikeRepo.existsByMemberIdAndReplyReplySeq(userId, replySeq);
		
		if (alreadyLiked) {
            // 좋아요 취소
            replyLikeRepo.deleteByMemberIdAndReplyReplySeq(userId, replySeq);
            replyRepo.decrementLikes(replySeq);
            return false; // 좋아요가 취소됨
        } else {
            // 좋아요 추가
            Reply reply = replyRepo.findById(replySeq)
                    .orElseThrow(() -> new Exception("댓글을 찾을 수 없습니다."));
            Member member = Member.builder().id(userId).build();
            ReplyLike replyLike = ReplyLike.builder()
                    .member(member)
                    .reply(reply)
                    .build();
            replyLikeRepo.save(replyLike);
            replyRepo.incrementLikes(replySeq);
            return true; // 좋아요가 추가됨
        }
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

	@Override
    public Reply getReplyBySeq(int replySeq) {
        return replyRepo.findById(replySeq)
                .orElseThrow(() -> new RuntimeException("해당 댓글을 찾을 수 없습니다."));
    }
	
	// 댓글 삭제
	@Override
	public void deleteReply(int replySeq) {
		replyRepo.deleteById(replySeq);

	}

}
