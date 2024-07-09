package com.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

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
	public String mainPage(HttpSession session, Model model, @ModelAttribute("message") String message,
            @ModelAttribute("text") String text, @ModelAttribute("messageType") String messageType) {
		Member loginUser = (Member) session.getAttribute("loginUser");
		
		if (loginUser == null) {
			return "main";
		}
		// 플래시 메시지를 모델에 추가 (필요한 경우)
	    if (message != null) {
	        model.addAttribute("message", message);
	        model.addAttribute("text", text);
	        model.addAttribute("messageType", messageType);
	    }
		
		return "main";
	}
}
