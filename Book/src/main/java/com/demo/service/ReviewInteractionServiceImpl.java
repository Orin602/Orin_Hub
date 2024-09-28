package com.demo.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.domain.Member;
import com.demo.domain.Review;
import com.demo.domain.ReviewInteraction;
import com.demo.domain.ReviewInteraction.InteractionType;
import com.demo.persistence.ReviewInteractionRepository;

@Service
public class ReviewInteractionServiceImpl implements ReviewInteractionService {

    @Autowired
    private ReviewInteractionRepository reviewInteractionRepo;

    @Transactional
    @Override
    public void addInteraction(Member member, Review review, InteractionType interactionType) {
        // 기존 상호작용이 있는지 확인
        ReviewInteraction existingInteraction = findByMemberAndReviewAndInteractionType(member, review, interactionType);
        
        if (existingInteraction == null) {
            // 존재하지 않으면 새로 추가
            ReviewInteraction interaction = new ReviewInteraction();
            interaction.setMember(member);
            interaction.setReview(review);
            interaction.setInteractionType(interactionType);
            interaction.setInteractionDate(new Date());
         
            // 로그 추가
            System.out.println("Saving new interaction: " + interaction);
            
            reviewInteractionRepo.save(interaction);
        } else {
            // 이미 존재하는 경우 삭제
            delete(existingInteraction);
        }
    }

    @Override
    public ReviewInteraction findByMemberAndReviewAndInteractionType(Member member, Review review, InteractionType interactionType) {
        return reviewInteractionRepo.findByMemberAndReviewAndInteractionType(member, review, interactionType);
    }

    @Override
    public void delete(ReviewInteraction interaction) {
        reviewInteractionRepo.delete(interaction);
    }

    @Override
    public List<ReviewInteraction> getRecommendationsByMemberId(String memberId) {
        // Repository 메서드를 호출하여 데이터베이스에서 추천 리뷰를 가져옴
        return reviewInteractionRepo.findRecommendationsByMemberId(memberId);
    }

    @Override
    public List<ReviewInteraction> getBookmarksByMemberId(String memberId) {
        // Repository 메서드를 호출하여 데이터베이스에서 즐겨찾기 리뷰를 가져옴
        return reviewInteractionRepo.findBookmarksByMemberId(memberId);
    }
}
