package com.demo.service;

import java.util.List;

import org.springframework.data.domain.Sort;

import com.demo.domain.Qna;

public interface QnaService {

	// Q&A 작성 (qna_date로 저장)
    Qna createQna(Qna qna);

    // Q&A 삭제
    void deleteQna(int qna_seq);

    // Q&A 수정 (qna_date로 저장)
    Qna updateQna(int qna_seq, Qna qna);

    // Q&A 전체 조회 (관리자용)
    List<Qna> getAllQnas(Sort sort);

    // Q&A 특정 조회 (회원용)
    List<Qna> getQnasByMemberId(String memberId);

    // 답변 작성 (member_code가 1인 회원만 가능, answer_date로 저장)
    Qna addAnswer(int qna_seq, String answer);
}
