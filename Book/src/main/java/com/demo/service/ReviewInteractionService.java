package com.demo.service;

import java.util.List;

import com.demo.domain.Member;
import com.demo.domain.Review;
import com.demo.domain.ReviewInteraction;
import com.demo.domain.ReviewInteraction.InteractionType;

public interface ReviewInteractionService {

	// 추천 또는 즐겨찾기 추가
    void addInteraction(Member member, Review review, InteractionType interactionType);

    // 특정 회원의 특정 리뷰에 대한 추천 또는 즐겨찾기 내역 조회
    ReviewInteraction findByMemberAndReviewAndInteractionType(Member member, Review review, InteractionType interactionType);
    
    // 추천 또는 즐겨찾기 삭제
    void delete(ReviewInteraction interaction);
    
    // 내 추천 리뷰 조회
    List<ReviewInteraction> getRecommendationsByMemberId(String memberId);

    // 내 즐겨찾기 리뷰 조회
    List<ReviewInteraction> getBookmarksByMemberId(String memberId);
}
