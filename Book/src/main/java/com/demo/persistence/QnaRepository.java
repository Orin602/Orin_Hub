package com.demo.persistence;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.demo.domain.Qna;

public interface QnaRepository extends JpaRepository<Qna, Integer> {

	// Fix Q&A 조회 (관리자용)
	@Query("SELECT q FROM Qna q WHERE q.member.membercode =1")
    List<Qna> findFixQna();
    
    // customer Q&A 조회
	@Query("SELECT q FROM Qna q WHERE q.member.membercode =0")
    List<Qna> findCustomerQna();
	
    // Q&A 특정 조회 (회원용)
    @Query("SELECT q FROM Qna q WHERE q.member.id = :id")
    public List<Qna> getMyQna(@Param("id") String id);
    
    // qna_seq로 QnA 조회
    @Query("SELECT q FROM Qna q WHERE q.qna_seq =:qna_seq")
    Qna findQnaBySeq(@Param("qna_seq") int qna_seq);
}
