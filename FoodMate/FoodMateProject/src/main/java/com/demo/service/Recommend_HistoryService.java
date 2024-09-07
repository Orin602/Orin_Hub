package com.demo.service;

import java.util.List;

import com.demo.domain.MemberData;
import com.demo.domain.Recommend_History;
import com.demo.domain.foodRecipe;

public interface Recommend_HistoryService { // 인터페이스 이름 수정

    List<Recommend_History> getMyRecommendHistory(MemberData member); // 메서드 시그니처 수정
//
//    List<RecommendData> getMyRecommendData(long idx); // 메서드 시그니처 수정

    void saveRecommendHistory(Recommend_History history); // 메서드 시그니처 수정

    void deleteRecommendHistory(int idx);
	}
