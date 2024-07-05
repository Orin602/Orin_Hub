package com.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.demo.domain.Member;
import com.demo.domain.Review;
import com.demo.service.ReviewService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ReviewController {

	@Autowired
	private ReviewService reviewService;
	
	// 리뷰 페이지
	@GetMapping("/review")
	public String reviewMain(@RequestParam(value="page", defaultValue="1") int page,
							@RequestParam(value="size", defaultValue="7") int size, Model model) {
		// 페이징 처리
		Page<Review> pageList = reviewService.getAllReview(page, size);
		List<Review> reviewList = pageList.getContent();
		
		model.addAttribute("reviewList", reviewList);
		model.addAttribute("currentPage", page);	// 현재 페이지
        model.addAttribute("totalPages", pageList.getTotalPages());	// 전체 페이지
        model.addAttribute("hasNext", pageList.hasNext());	// 다음 페이지
        model.addAttribute("hasPrevious", pageList.hasPrevious());	// 이전 페이지
		return "review/bookReview";
	}
	
	// 리뷰 작성 화면
	@GetMapping("/reviewWriteForm")
	public String reviewWriteView(HttpSession session, Model model) {
		Member loginUser = (Member)session.getAttribute("loginUser");
		
		if(loginUser == null) {
			model.addAttribute("message", "로그인 후 이용 가능합니다.");
			model.addAttribute("messageType", "info");
			return "redirect:/login";
		}
		
		return "review/reviewWrite";
	}
	
	@GetMapping("/checkLogin")
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> checkLoginStatus(HttpSession session) {
        Member loginUser = (Member) session.getAttribute("loginUser");
        boolean loggedIn = (loginUser != null);

        Map<String, Boolean> response = new HashMap<>();
        response.put("loggedIn", loggedIn);

        return ResponseEntity.ok().body(response);
    }
	
	// 리뷰 작성 처리
	@PostMapping("/review-write")
	public String reviewWriteAction(HttpSession session, @RequestParam("title") String title,
									@RequestParam("content") String content, Model model) {
		Member loginUser = (Member) session.getAttribute("loginUser");
		
		if(loginUser == null) {
			model.addAttribute("message", "로그인 후 이용 가능합니다.");
			model.addAttribute("messageType", "info");
			return "redirect:/login";
		}
		Review review = new Review();
		review.setTitle(title);
		review.setContent(content);
		review.setMember(loginUser);
		
		reviewService.insertReview(review);
		
		return "redirect:/review"; // 작성한 리뷰 목록 페이지로 리다이렉트
	}
	
	// 리뷰 상세 화면
	@GetMapping("reviewDetail")
	public String reviewDetailView(@RequestParam("review_seq") int review_seq,
									Model model, HttpSession session) {
		Member loginUser = (Member) session.getAttribute("loginUser");
		
		// 특정 리뷰 조회
        Review review = reviewService.getReview(review_seq);
        
        // 모델에 리뷰와 로그인 사용자 정보 추가
        model.addAttribute("review", review);
        model.addAttribute("loginUser", loginUser);

        return "review/reviewDetail";
	}
	
	// 리뷰 수정 화면
	@GetMapping("/edit")
    public String reviewEditView(@RequestParam("reviewSeq") int reviewSeq,
									Model model, HttpSession session) {
        Review review = reviewService.getReview(reviewSeq);
        model.addAttribute("review", review);
        return "review/reviewEdit";
    }
	// 리뷰 수정 처리
    @PostMapping("/edit")
    @ResponseBody
    public ResponseEntity<String> handleReviewEdit(@ModelAttribute("review") Review vo) {
        try {
            reviewService.updateReview(vo);
            return ResponseEntity.ok("수정이 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("수정 중 오류가 발생했습니다.");
        }
    }
    
    
    
}

