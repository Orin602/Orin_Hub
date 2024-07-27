package com.demo.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.domain.SearchHistory;
import com.demo.persistence.SearchHistoryRepository;

@Service
public class SearchHistoryServiceImpl implements SearchHistoryService {

	@Autowired
	private SearchHistoryRepository searchHistoryRepo;
	
	@Override
    public void saveSearchHistory(SearchHistory searchHistory) {
		searchHistoryRepo.save(searchHistory);
    }
	
	@Override
	public List<Object[]> getTopSearchQueries() {
		
		return searchHistoryRepo.findTopSearchQueries();
	}

	@Override
	public List<Object[]> getRelatedSearchQueries(String searchTerm) {
		
		return searchHistoryRepo.findRelatedSearchQueries(searchTerm);
	}

	@Override
	public List<SearchHistory> getSearchHistoryByMemberId(String id) {
		
		return searchHistoryRepo.findMemberById(id);
	}

	@Override
	public List<SearchHistory> getSearchHistoryByDateRange(Date startDate, Date endDate) {
		
		return searchHistoryRepo.findByDateRange(startDate, endDate);
	}

}
