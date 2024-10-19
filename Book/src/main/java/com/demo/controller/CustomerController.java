package com.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.demo.domain.Member;
import com.demo.domain.Qna;
import com.demo.service.NoticeService;
import com.demo.service.QnaService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CustomerController {
	
	@Autowired
	private NoticeService noticeService;
	@Autowired
	private QnaService qnaService;

	@GetMapping("/customer")
	public String customerMainView() {
		
		
		return "customer/customerMain";
	}
	
	@GetMapping("/notice")
	public String noticeView() {
		
		
		return "customer/notice";
	}
	
	@GetMapping("/fav_qna")
	public String qnaView() {
		
		
		return "customer/qna";
	}
	
	//qna write
	@GetMapping("/qna_write")
	public String qnaWriteView(HttpSession session, Model model) {	//질문 작성 페이지
		Member loginUser = (Member)session.getAttribute("loginUser");
		
		if(loginUser == null) {
			model.addAttribute("message", "로그인 페이지로 이동");
			model.addAttribute("text", "질문 작성은 로그인 후 이용 가능합니다.");
			model.addAttribute("messageType", "info");
			
			return "login/login";
		}
		
		return "customer/qna_write";
	}
	@PostMapping("/qna-write-action")
	public String qnaWriteAction(HttpSession session, Qna qna) {
		Member loginUser = (Member) session.getAttribute("loginUser");
		
		qna.setMember(loginUser);
		qnaService.createQna(qna);
		
		return "customer/qna";
	}
	
	//qna delete
	
	//qna update
	
	
	
}
