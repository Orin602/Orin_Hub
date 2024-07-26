package com.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.demo.domain.Member;
import com.demo.dto.BookRecommendation;
import com.demo.service.BookRecommendationService;

import jakarta.servlet.http.HttpSession;
import jakarta.xml.bind.JAXBException;

@Controller
public class MainController {
	
	@Autowired
	private BookRecommendationService bookRecoService;
	
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

		// 데이터 조회 및 모델에 추가
		try {
            List<BookRecommendation> recommendations = bookRecoService.getBookRecommendations();
            model.addAttribute("recommendations", recommendations);
        } catch (JAXBException e) {
            model.addAttribute("error", "Failed to fetch book recommendations.");
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
