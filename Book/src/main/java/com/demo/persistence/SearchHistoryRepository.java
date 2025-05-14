package com.demo.persistence;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.demo.domain.SearchHistory;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Integer> {

    // 인기 검색어
    @Query("SELECT s.query, COUNT(s.query) as frequency FROM SearchHistory s " +
           "GROUP BY s.query ORDER BY frequency DESC")
    List<Object[]> findTopSearchQueries();
    
    // 연관 검색어
    @Query("SELECT s.query, COUNT(s.query) as frequency FROM SearchHistory s " +
           "WHERE s.query LIKE %:searchTerm% AND s.query != :searchTerm " +
           "GROUP BY s.query ORDER BY frequency DESC")
    List<Object[]> findRelatedSearchQueries(@Param("searchTerm") String searchTerm);
    
    // 특정 회원의 검색 기록
    @Query("SELECT s FROM SearchHistory s WHERE s.member.id = :id ORDER BY s.search_date DESC")
    List<SearchHistory> findMemberById(@Param("id") String id);
    
    // 특정 기간 동안의 검색 기록
    @Query("SELECT s FROM SearchHistory s WHERE s.search_date BETWEEN :startDate AND :endDate ORDER BY s.search_date DESC")
    List<SearchHistory> findByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
