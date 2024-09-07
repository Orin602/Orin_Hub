package com.demo.service;

import java.util.List;

import com.demo.domain.AdminQnaBoard;
import com.demo.domain.MemberData;
import com.demo.domain.askBoard;

// CustomerService: 고객 서비스 관련 기능을 정의하는 서비스 인터페이스
public interface CustomerService {
	
	// QnA 목록을 가져오는 메서드
	// 반환값 : QnA 목록의 문자열 리스트
    List<String> getQnAList();
    
    // 문의를 추가하는 메서드
    // 매개변수 : 추가할 문의(askBoard 객체)
    // 반환값 : 없음
    void addInquiry(askBoard inquiry);
    
    // 특정 회원의 문의 목록을 가져오는 메서드
    // 매개변수 : 로그인한 회원의 정보 (MemberData 객체)
    // 반환값 : 해당 회원의 문의 목록
    List<askBoard> getInquiryList(MemberData loginUser);
    
    // 주어진 제목에 따른 문의 목록을 가져오는 메서드
    // 매개변수 : 제목
    // 반환값 : 제목에 해당하는 문의 목록
    List<askBoard> getInquiriesBySubject(String subject);
    
    // Named 쿼리를 사용하여 주어진 주제에 따른 문의 목록을 가져오는 메서드
    List<askBoard> getInquiriesBySubjectNamedQuery(String name);
    
    // 모든 Q&A 게시물을 가져오는 메서드
    List<AdminQnaBoard> getAllQnaBoards();
    
    // 주어진 질문에 대한 상세 정보를 가져오는 메서드
	AdminQnaBoard getQnaDetailsById(String question);
	
	// 주어진 ID에 대한 Q&A 게시물의 상세 정보를 가져오는 메서드
	AdminQnaBoard getQnaDetailsById(Long id);
	
	// 주어진 ID에 대한 문의의 상세 정보를 가져오는 메서드
	askBoard getInquiryDetailsById(Long id);
	
	// 주어진 문의 객체에 대한 ID로 문의를 조회하는 메서드
	askBoard findById(askBoard inquiry);
	
	// 모든 문의 목록을 가져오는 메서드
	List<askBoard> getInquiryList();
	

}
