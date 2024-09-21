package com.demo.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.domain.Member;
import com.demo.domain.Review;
import com.demo.domain.ReviewInteraction;
import com.demo.domain.ReviewInteraction.InteractionType;

public interface ReviewInteractionRepository extends JpaRepository<ReviewInteraction, Integer> {
	
	ReviewInteraction findByMemberAndReviewAndInteractionType(Member member, Review review, InteractionType interactionType);
}
