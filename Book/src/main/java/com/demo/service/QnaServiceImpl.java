package com.demo.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.demo.domain.Qna;
import com.demo.persistence.QnaRepository;

@Service
public class QnaServiceImpl implements QnaService {
	
	@Autowired
	private QnaRepository qnaRepo;

	@Override
    public Qna createQna(Qna qna) {
        qna.setQna_date(new Date()); // 작성 날짜 설정
        return qnaRepo.save(qna);
    }

    @Override
    public void deleteQna(int qna_seq) {
        Qna qna = qnaRepo.findById(qna_seq).orElseThrow(() ->
            new IllegalArgumentException("해당 Q&A를 찾을 수 없습니다."));
        qnaRepo.delete(qna);
    }

    @Override
    public Qna updateQna(int qna_seq, Qna qna) {
        Qna existingQna = qnaRepo.findById(qna_seq).orElseThrow(() ->
            new IllegalArgumentException("해당 Q&A를 찾을 수 없습니다."));
        // 질문에 이미 답변이 존재하는지 확인
        if (existingQna.getAnswer() != null) {
            throw new IllegalArgumentException("답변이 존재하는 질문은 수정할 수 없습니다.");
        }
        existingQna.setTitle(qna.getTitle());
        existingQna.setContent(qna.getContent());
        existingQna.setQna_date(new Date()); // 수정 시 현재 날짜로 설정
        return qnaRepo.save(existingQna);
    }

    @Override
    public List<Qna> getAllQnas(Sort sort) {
        return qnaRepo.findAll(sort);
    }

    @Override
    public List<Qna> getQnasByMemberId(String memberId) {
        return qnaRepo.findByMemberId(memberId);
    }

    @Override
    public Qna addAnswer(int qna_seq, String answer) {
        Qna qna = qnaRepo.findById(qna_seq).orElseThrow(() ->
            new IllegalArgumentException("해당 Q&A를 찾을 수 없습니다."));
        
        qna.setAnswer(answer);
        qna.setAnswer_date(new Date()); // 답변 날짜 설정
        return qnaRepo.save(qna);
    }
}
