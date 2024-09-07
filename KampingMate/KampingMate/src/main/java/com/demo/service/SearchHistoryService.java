package com.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.domain.GoCamping;
import com.demo.persistence.SearchHistoryRepository;

@Service
public class SearchHistoryService {

    @Autowired
    private SearchHistoryRepository searchHistoryRepository;

    public void insertHistoryItem(int contentId, Long no_data) {
        if (!searchHistoryRepository.existsByGocampingContentIdAndMemberNo_data(contentId, no_data)) {
            searchHistoryRepository.insertHistoryItem(contentId, no_data);
        }
    }
    
    public List<GoCamping> getSearchHistoryForMember(Long no_data) {
        return searchHistoryRepository.findAllCampingByMemberNoData(no_data);
    }
    
    public List<GoCamping> getRecommendationsForMember(Long no_data) {
        // Simple recommendation logic based on search history
        return searchHistoryRepository.findAllCampingByMemberNoData(no_data);
    }
}
