package com.demo.persistence;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.demo.domain.Qna;

public interface QnaRepository extends JpaRepository<Qna, Integer> {

	// Q&A 전체 조회 (관리자용)
    List<Qna> findAll(Sort sort);
    
    // Q&A 특정 조회 (회원용)
    @Query("SELECT q FROM Qna q WHERE q.member.id = :id")
    List<Qna> findByMemberId(@Param("id") String id);
}
