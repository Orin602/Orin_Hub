package com.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.domain.AdminQnaBoard;
import com.demo.domain.MemberData;
import com.demo.domain.askBoard;
import com.demo.persistence.AdminQnaBoardRepository;
import com.demo.persistence.CustomerServiceRepository;

@Service
public class CustomerServiceImpl implements CustomerService {
	
	@Autowired
	AdminQnaBoardRepository qnaBoardRepository;
	
	@Autowired
	CustomerServiceRepository customerServiceRepository;
    
	// 생성자를 통한 의존성 주입
    public CustomerServiceImpl(AdminQnaBoardRepository qnaBoardRepository) {
        this.qnaBoardRepository = qnaBoardRepository;
    }

    /**
     * 모든 Q&A 게시물을 가져오는 메서드
     * @return 모든 Q&A 게시물 목록
     */
    @Override
    public List<AdminQnaBoard> getAllQnaBoards() {
        return qnaBoardRepository.findAll();
    }

    @Override
    public List<String> getQnAList() {
        
        return null;
    }

    /**
     * 문의를 추가하는 메서드
     * @param inquiry 추가할 문의
     */
    @Override
    public void addInquiry(askBoard inquiry) {
    	inquiry.setStatus("답변 대기"); // 문의 상태 설정
    	customerServiceRepository.save(inquiry); // 문의 저장
    }

    /** 
     * 모든 문의 목록을 가져오는 메서드
     * @return 모든 문의 목록
     */
    @Override
    public List<askBoard> getInquiryList() {
        
        return customerServiceRepository.getInquiryList();
    }

    @Override
    public List<askBoard> getInquiriesBySubject(String subject) {
        
        return null;
    }

    /**
     * 특정 ID를 가진 Q&A 게시물의 상세 정보를 가져오는 메서드
     * @param id 게시물 ID
     * @return Q&A 게시물 상세 정보
     */
    @Override
    public List<askBoard> getInquiriesBySubjectNamedQuery(String subject) {
        
        return null;
    }

	public AdminQnaBoard getQnaDetailsById(Long id) {
		return qnaBoardRepository.findById(id).orElse(null);
	}

	@Override
	public AdminQnaBoard getQnaDetailsById(String question) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public askBoard getInquiryDetailsById(Long id) {
		return customerServiceRepository.findById(id).orElse(null);
	}

	@Override
	public askBoard findById(askBoard inquiry) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<askBoard> getInquiryList(MemberData loginUser) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
