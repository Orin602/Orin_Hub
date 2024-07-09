package com.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.demo.domain.Member;

import jakarta.servlet.http.HttpSession;

@Controller
public class ReviewController {

	// 리뷰 메인 화면
	@GetMapping("/review")
	public String reviewMain(Model model, HttpSession session) {
		Member loginUser = (Member)session.getAttribute("loginUser");
		
		return "review/reviewMain";
	}
	
	// 리뷰 작성 화면
	@GetMapping("/review-write")
	public String reviewWriteView(Model model, HttpSession session) {
		Member loginUser = (Member)session.getAttribute("loginUser");
		
		if(loginUser == null) {
			model.addAttribute("message", "로그인 페이지로 이동");
			model.addAttribute("text", "리뷰 작성은 로그인 후 이용 가능합니다.");
			model.addAttribute("messageType", "info");
			
			return "login/login";
		}
		
		return "review/reviewWrite";
	}
}
