package com.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.demo.domain.Member;

import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {
	
	// 초기 화면
	@GetMapping("/")
	public String indexPage() {
		return "index";
	}

	// 메인 화면
	@GetMapping("/main")
	public String mainPage(HttpSession session, Model model) {
		Member loginUser = (Member)session.getAttribute("loginUser");
		
		if (loginUser == null) {
			return "main";
		}
		model.addAttribute("nickname", loginUser.getNickname());
		
		return "main";
	}
}
