package com.demo.service;

import java.util.List;

import org.springframework.data.domain.Sort;

import com.demo.domain.Qna;

public interface QnaService {

	// QnA를 seq로 찾는 메서드 정의
	Qna findQnaBySeq(int qna_seq); 
	
	// Q&A 작성 (qna_date로 저장)
    Qna createQna(Qna qna);
    // 고정 질문 작성
    Qna createFixQna(Qna qna);

    // Q&A 삭제
    void deleteQna(int qna_seq);

    // Q&A 수정 (qna_date로 저장)
    void updateQna(Qna qna);

    // Q&A 특정 조회 (회원용)
    List<Qna> getMyQna(String id);

    // 답변 작성 (member_code가 1인 회원만 가능, answer_date로 저장)
    Qna addAnswer(int qna_seq, String answer);
    
    // qna 조회
    List<Qna> getFixQna(); // 관리자용 고정 질문 조회
    List<Qna> getCustomerQna(); // 고객용 질문 조회

}
