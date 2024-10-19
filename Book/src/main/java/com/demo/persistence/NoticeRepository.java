package com.demo.persistence;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.domain.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Integer> {

	//공지사항 sort별 정렬
	List<Notice> findAll(Sort sort);
	
	
}
