package com.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.demo.domain.Member;
import com.demo.domain.Review;
import com.demo.service.ReplyService;
import com.demo.service.ReviewService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MypageController {

	@Autowired
	private ReviewService reviewService;
	@Autowired
	private ReplyService replyService;
	
	
	// 마이페이지
	@GetMapping("/mypage")
	public String mypageView(HttpSession session, Model model) {
		Member loginUser = (Member)session.getAttribute("loginUser");
		List<Review> myReview = reviewService.getByIdReview(loginUser.getId());
		
		model.addAttribute("myReview", myReview);
		model.addAttribute("loginUser", loginUser);
		
		return "mypage/mypageMain";
	}
}
