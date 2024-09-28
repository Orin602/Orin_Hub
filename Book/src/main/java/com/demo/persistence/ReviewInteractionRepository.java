package com.demo.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.demo.domain.Member;
import com.demo.domain.Review;
import com.demo.domain.ReviewInteraction;
import com.demo.domain.ReviewInteraction.InteractionType;

public interface ReviewInteractionRepository extends JpaRepository<ReviewInteraction, Integer> {
	
	ReviewInteraction findByMemberAndReviewAndInteractionType(Member member, Review review, InteractionType interactionType);
	
	//마이페이지용
	//내 추천 리뷰
	@Query("SELECT ri FROM ReviewInteraction ri WHERE ri.member.id = :id AND ri.interactionType = com.demo.domain.ReviewInteraction.InteractionType.RECOMMENDATION ORDER BY interactionDate DESC")
	List<ReviewInteraction> findRecommendationsByMemberId(@Param("id") String id);

	//내 즐겨찾기 리뷰
	@Query("SELECT ri FROM ReviewInteraction ri WHERE ri.member.id = :id AND ri.interactionType = com.demo.domain.ReviewInteraction.InteractionType.BOOKMARK ORDER BY interactionDate DESC")
	List<ReviewInteraction> findBookmarksByMemberId(@Param("id") String id);

}
