package com.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.domain.MemberData;
import com.demo.domain.Recommend_History;
import com.demo.domain.foodRecipe;
import com.demo.dto.RecommendData;
import com.demo.persistence.Recommend_HistoryRepository;

@Service
public class Recommend_HistoryServiceImpl implements Recommend_HistoryService { // 클래스 이름 수정

    @Autowired
    private Recommend_HistoryRepository recommendHistoryRepository;

    @Override
    public void saveRecommendHistory(Recommend_History history) {
        recommendHistoryRepository.save(history);
    
    }
//
    @Override
    public List<Recommend_History> getMyRecommendHistory(MemberData member) { // 메서드 시그니처 수정
        return recommendHistoryRepository.getMyRecommendHistoryById(member);
    }
	@Override
	public void deleteRecommendHistory(int idx) {
		recommendHistoryRepository.deleteById(idx);;
		
	}
	

//    @Override
//    public List<RecommendData> getMyRecommendData(long idx) { // 메서드 시그니처 수정
//       List<RecommendData> recommendDataList = 
//       List<Recommend_History> recommendHistoryList = recommendHistoryRepository.getMyRecommendHistoryById(id);
//
//        for (Recommend_History history : recommendHistoryList) {
//            RecommendData recommendData = new RecommendData();
//            recommendData.setIdx(String.valueOf(history.getRecommendFood().getIdx())); // 메서드 이름 수정
//            recommendData.setName(history.getRecommendFood().getName()); // 메서드 이름 수정
//            recommendData.setCalories(String.valueOf(history.getRecommendFood().getCalories())); // 메서드 이름 수정
//            recommendData.setImages(history.getRecommendFood().getImages()); // 메서드 이름 수정
//
//            recommendDataList.add(recommendData);
//        }
//
//        return recommendDataList;
//    }

	
}
