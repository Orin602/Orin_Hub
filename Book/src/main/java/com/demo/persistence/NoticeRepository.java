package com.demo.persistence;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.demo.domain.Notice;
import com.demo.domain.Review;

public interface NoticeRepository extends JpaRepository<Notice, Integer> {

	//공지사항 sort별 정렬
	List<Notice> findAll(Sort sort);
	
	// 리뷰 상세 조회
    @Query(value = "SELECT * FROM notice WHERE notice_seq = :notice_seq", nativeQuery = true)
    Notice getNoticeBySeq(@Param("notice_seq") int notice_seq);
	
}
