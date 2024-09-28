package com.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.demo.domain.Member;
import com.demo.domain.Reply;
import com.demo.domain.Review;
import com.demo.domain.ReviewInteraction;
import com.demo.service.ReplyService;
import com.demo.service.ReviewInteractionService;
import com.demo.service.ReviewService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MypageController {

	@Autowired
	private ReviewService reviewService;
	@Autowired
	private ReplyService replyService;
	@Autowired
	private ReviewInteractionService riService; 
	
	
	// 마이페이지
	@GetMapping("/mypage")
	public String mypageView(HttpSession session, Model model) {
		Member loginUser = (Member)session.getAttribute("loginUser");
		List<Review> myReview = reviewService.getByIdReview(loginUser.getId());
		// 내 추천
		List<ReviewInteraction> myRecoment = riService.getRecommendationsByMemberId(loginUser.getId());
		// 내 즐겨찾기
		List<ReviewInteraction> myBookMark = riService.getBookmarksByMemberId(loginUser.getId());
		// 내 댓글
		List<Reply> myReply = replyService.getReplyByMember(loginUser.getId());
		
		model.addAttribute("myReview", myReview);
		model.addAttribute("loginUser", loginUser);
		model.addAttribute("myRecoment", myRecoment);
		model.addAttribute("myBookMark", myBookMark);
		model.addAttribute("myReply", myReply);
		
		return "mypage/mypageMain";
	}
}
