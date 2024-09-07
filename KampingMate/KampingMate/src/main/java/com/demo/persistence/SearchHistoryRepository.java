package com.demo.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.demo.domain.GoCamping;
import com.demo.domain.SearchHistory;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Integer> {
	
	 @Query("SELECT CASE WHEN COUNT(sh) > 0 THEN true ELSE false END FROM SearchHistory sh WHERE sh.gocamping.contentId = :contentId AND sh.member.no_data = :no_data")
	 boolean existsByGocampingContentIdAndMemberNo_data(@Param("contentId") int contentId, @Param("no_data") Long no_data);

	 @Modifying
	 @Transactional
	 @Query(value="INSERT INTO Search_History (history_id, content_Id, no_data) VALUES (HISTORY_SEQ.nextval, :contentId, :no_data)", nativeQuery = true)
	 void insertHistoryItem(@Param("contentId") int contentId, @Param("no_data") Long no_data);

	 @Query("SELECT sh.gocamping FROM SearchHistory sh WHERE sh.member.no_data = :no_data")
	 List<GoCamping> findAllCampingByMemberNoData(@Param("no_data") Long no_data);
}
