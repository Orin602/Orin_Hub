package com.demo.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.domain.RecoBook;

public interface RecoBookRepository extends JpaRepository<RecoBook, Long> {

	// 회원 ID로 추천 기록 조회
    List<RecoBook> findByMemberId(String id);
}
