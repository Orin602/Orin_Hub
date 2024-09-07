package com.demo.service;

import java.util.List;

import com.demo.domain.AdminQnaBoard;
import com.demo.domain.MemberData;
import com.demo.domain.askBoard;


public interface CustomerService {
    List<String> getQnAList();
    void addInquiry(askBoard inquiry);
    List<askBoard> getInquiryList(MemberData loginUser);
    List<askBoard> getInquiriesBySubject(String subject);
    List<askBoard> getInquiriesBySubjectNamedQuery(String name);
    List<AdminQnaBoard> getAllQnaBoards();
	AdminQnaBoard getQnaDetailsById(String question);
	AdminQnaBoard getQnaDetailsById(Long id);
	askBoard getInquiryDetailsById(Long id);
	askBoard findById(askBoard inquiry);
	List<askBoard> getInquiryList();
	

}
