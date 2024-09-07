package com.demo.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.demo.domain.MemberData;
import com.demo.domain.Recommend_History;
import com.demo.domain.foodRecipe;

public interface Recommend_HistoryRepository extends JpaRepository<Recommend_History, Integer> {

	// 회원별 추천받은 음식(레시피)기록 조회
	@Query("SELECT rh FROM Recommend_History rh WHERE rh.memberData =:member")
	public List<Recommend_History> getMyRecommendHistoryById(MemberData member);
	
	List<Recommend_History> findByMemberDataAndRecommendFood(MemberData memberData, foodRecipe recommendFood);
	
}