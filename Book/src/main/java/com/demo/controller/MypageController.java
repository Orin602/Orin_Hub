package com.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.demo.domain.Member;
import com.demo.domain.Qna;
import com.demo.domain.Reply;
import com.demo.domain.Review;
import com.demo.domain.ReviewInteraction;
import com.demo.service.QnaService;
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
	@Autowired
	private QnaService qnaService;
	
	
	// 마이페이지
	@GetMapping("/mypage")
	public String mypageView(HttpSession session, Model model) {
		Member loginUser = (Member)session.getAttribute("loginUser");
		
		model.addAttribute("loginUser", loginUser);
		
		return "mypage/mypageMain";
	}
	
	// 내가 작성한 리뷰
	@GetMapping("/myreview")
	public String myReviewView(HttpSession session, Model model) {
		Member loginUser = (Member)session.getAttribute("loginUser");
		List<Review> myReview = reviewService.getByIdReview(loginUser.getId());
		
		model.addAttribute("myReview", myReview);
		
		return "mypage/myReview";
	}
	
	// 내가 추천한 리뷰
	@GetMapping("/myrecoment")
	public String myRecomentView(HttpSession session, Model model) {
		Member loginUser = (Member)session.getAttribute("loginUser");
		List<ReviewInteraction> myRecoment = riService.getRecommendationsByMemberId(loginUser.getId());
		
		model.addAttribute("myRecoment", myRecoment);
		
		return "mypage/myRecoment";
	}
	
	// 내가 즐겨찾기한 리뷰
	@GetMapping("/mybookmark")
	public String myBookMarkView(HttpSession session, Model model) {
		Member loginUser = (Member)session.getAttribute("loginUser");
		List<ReviewInteraction> myBookMark = riService.getBookmarksByMemberId(loginUser.getId());
		
		model.addAttribute("myBookMark", myBookMark);
		
		return "mypage/myBookMark";
	}
	// 내가 작성한 댓글
	@GetMapping("/myreply")
	public String myReplyView(HttpSession session, Model model) {
		Member loginUser = (Member)session.getAttribute("loginUser");
		List<Reply> myReply = replyService.getReplyByMember(loginUser.getId());
		
		model.addAttribute("myReply", myReply);
		
		return "mypage/myReply";
	}
	
	// 내 질문 현황
	@GetMapping("/myqna")
	public String myQnaView(HttpSession session, Model model) {
		Member loginUser = (Member)session.getAttribute("loginUser");
		
		if(loginUser == null) {
			return "login/login";
		}
		model.addAttribute("loginUser", loginUser);
		List<Qna> myQna = qnaService.getMyQna(loginUser.getId());
		
		model.addAttribute("myQna", myQna);
		
		return "mypage/myQna";
	}
}
