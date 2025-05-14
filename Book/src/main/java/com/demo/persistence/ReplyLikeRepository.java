package com.demo.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.domain.ReplyLike;

public interface ReplyLikeRepository extends JpaRepository<ReplyLike, Integer> {
	boolean existsByMemberIdAndReplyReplySeq(String userId, int replySeq);
    void deleteByMemberIdAndReplyReplySeq(String userId, int replySeq);
}
