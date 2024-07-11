package com.demo.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.demo.domain.Member;
import com.demo.domain.Review;
import com.demo.service.ReviewService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ReviewController {

	@Autowired
	private ReviewService reviewService;
	
	@Value("${com.demo.upload.path}")
	private String uploadPath;
	
	// 리뷰 메인 화면
	@GetMapping("/review")
	public String reviewMain(Model model, HttpSession session) {
		List<Review> reviews = reviewService.getAllReview();
		
		model.addAttribute("reviews", reviews);
		
		return "review/reviewMain";
	}
	
	// 검색 조건 처리
	@GetMapping("/search_review")
	public String SearchReviews(Model model, @RequestParam("searchType") String searchType,
			@RequestParam("keyword") String keyword) {
		
		List<Review> reviews;
		
		if(searchType.equals("search_id")) {
			reviews = reviewService.getByIdReview(keyword);
		} else if(searchType.equals("search_title")) {
			reviews = reviewService.getByTitleReview(keyword);
		} else {
			reviews = List.of();	// 빈 리스트 반환
		}
		model.addAttribute("reviews", reviews);
        model.addAttribute("searchType", searchType);
        model.addAttribute("keyword", keyword);
        
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
	
	// 리뷰 작성 처리
	@PostMapping("/review-write-action")
	public String reviewWriteAction(Review vo, Model model, HttpSession session,
			@RequestParam("uploadFile") MultipartFile uploadFile) {
		Member loginUser = (Member)session.getAttribute("loginUser");
		
		if(loginUser == null) {
			model.addAttribute("message", "로그인 페이지로 이동");
			model.addAttribute("text", "리뷰 작성은 로그인 후 이용 가능합니다.");
			model.addAttribute("messageType", "info");
			
			return "login/login";
		}
		if(!uploadFile.isEmpty()) {
			try {
				// 파일 저장
				String originalFilename = uploadFile.getOriginalFilename();
				String fileName = UUID.randomUUID().toString() + originalFilename;
				Path filePath = Paths.get(uploadPath + fileName);
				uploadFile.transferTo(filePath.toFile());
				
				// 파일 경로
				vo.setUploadFilePath(filePath.toString());
			} catch (IOException e) {
				e.printStackTrace();
				model.addAttribute("message", "파일 업로드 실패");
				model.addAttribute("text", "파일 업로드 중 오류가 발생했습니다.");
				model.addAttribute("icon", "error");
				return "review/reviewWrite";
			}
		}
		vo.setMember(loginUser); // 로그인한 사용자 정보를 리뷰에 설정
        reviewService.insertReview(vo);
		
		return "redirect:/review";
	}
}
