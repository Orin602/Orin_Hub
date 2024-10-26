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
    public void updateQna(Qna qna) {
        Qna update_qna = qnaRepo.findQnaBySeq(qna.getQna_seq());
        qna.setQna_seq(update_qna.getQna_seq());	// 질문 고유번호 유지
        qna.setQna_date(update_qna.getQna_date());	// 질문 작성시간 유지
        qna.setMember(update_qna.getMember());		// 작성자 유지
        
        qnaRepo.save(qna);        
    }

    @Override
    public List<Qna> getAllQnas(Sort sort) {
        return qnaRepo.findAll(sort);
    }

    @Override
    public Qna addAnswer(int qna_seq, String answer) {
        Qna qna = qnaRepo.findById(qna_seq).orElseThrow(() ->
            new IllegalArgumentException("해당 Q&A를 찾을 수 없습니다."));
        
        // 관리자만 답변을 작성할 수 있도록 체크 (member_code == 1)
        if (qna.getMember().getMembercode() != 1) {
            throw new IllegalArgumentException("관리자만 답변을 작성할 수 있습니다.");
        }
        
        qna.setAnswer(answer);
        qna.setAnswer_date(new Date()); // 답변 날짜 설정
        qna.setAnswer_status(1);
        
        return qnaRepo.save(qna);
    }

	@Override
	public List<Qna> getMyQna(String id) {
		return qnaRepo.getMyQna(id);
	}

	@Override
	public Qna findQnaBySeq(int qna_seq) {
		return qnaRepo.findQnaBySeq(qna_seq);
	}

	
}
