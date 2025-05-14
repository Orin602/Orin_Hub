package com.demo.service;

import java.util.Date;
import java.util.List;

import com.demo.domain.SearchHistory;

public interface SearchHistoryService {

	// 인기 검색어 목록 조회
    List<Object[]> getTopSearchQueries();

    // 연관 검색어 목록 조회
    List<Object[]> getRelatedSearchQueries(String searchTerm);

    // 특정 회원의 검색 기록 조회
    List<SearchHistory> getSearchHistoryByMemberId(String id);

    // 특정 기간 동안의 검색 기록 조회
    List<SearchHistory> getSearchHistoryByDateRange(Date startDate, Date endDate);

	void saveSearchHistory(SearchHistory searchHistory);
}
