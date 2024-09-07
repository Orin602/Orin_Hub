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
    
    public CustomerServiceImpl(AdminQnaBoardRepository qnaBoardRepository) {
        this.qnaBoardRepository = qnaBoardRepository;
    }

    @Override
    public List<AdminQnaBoard> getAllQnaBoards() {
        return qnaBoardRepository.findAll();
    }

    @Override
    public List<String> getQnAList() {
        
        return null;
    }

    @Override
    public void addInquiry(askBoard inquiry) {
    	inquiry.setStatus("답변 대기");
    	customerServiceRepository.save(inquiry);
    }

    @Override
    public List<askBoard> getInquiryList() {
        
        return customerServiceRepository.getInquiryList();
    }

    @Override
    public List<askBoard> getInquiriesBySubject(String subject) {
        
        return null;
    }

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
