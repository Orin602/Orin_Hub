package com.demo.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
	
	@PostMapping("/review-write")
	public String reviewWriteAction(HttpSession session, @RequestParam("title") String title, Model model,
	                                @RequestParam("content") String content, @RequestParam("imageFile") MultipartFile imageFile) {
	    Member loginUser = (Member) session.getAttribute("loginUser");

	    // 세션에서 로그인 사용자 정보 가져오기
	    if (loginUser == null) {
	        model.addAttribute("message", "로그인 후 이용 가능합니다.");
	        model.addAttribute("messageType", "info");
	        return "redirect:/login"; // 로그인 페이지로 리다이렉트
	    }

	    // 이미지 파일 업로드 여부 확인
	    if (!imageFile.isEmpty()) {
	        try {
	            // 업로드할 파일의 저장 경로 설정 (uploads 폴더)
	            String uploadDir = uploadPath;
	            String originalFileName = imageFile.getOriginalFilename();
	            String filePath = uploadDir + originalFileName;

	         // 파일을 지정한 경로에 저장
                byte[] bytes = imageFile.getBytes();
                Path path = Paths.get(filePath);
                Files.write(path, bytes);

	            // 파일 경로를 리뷰 객체에 설정 (이미지 파일 경로 저장)
	            String imagePath = "uploads/" + originalFileName;

	            // Review 객체 생성 및 데이터 설정
	            Review review = new Review();
	            review.setTitle(title);
	            review.setContent(content);
	            review.setImagePath(imagePath);
	            review.setMember(loginUser);

	            // Review 저장
	            reviewService.insertReview(review);

	            // 성공 메시지 설정
	            model.addAttribute("message", "리뷰가 성공적으로 작성되었습니다.");
	            model.addAttribute("messageType", "success");

	            // 리뷰 목록 페이지로 리다이렉트
	            return "redirect:/review";
	        } catch (IOException e) {
	            // 파일 업로드 중 예외 처리
	            model.addAttribute("message", "파일 업로드 중 오류가 발생했습니다.");
	            model.addAttribute("messageType", "error");
	            return "redirect:/reviewWriteForm"; // 리뷰 작성 폼으로 리다이렉트
	        }
	    } else {
	        // 이미지 파일이 선택되지 않은 경우
	        // Review 객체 생성 및 데이터 설정 (이미지 없이 리뷰 저장)
	        Review review = new Review();
	        review.setTitle(title);
	        review.setContent(content);
	        review.setMember(loginUser);

	        // Review 저장
	        reviewService.insertReview(review);

	        // 성공 메시지 설정
	        model.addAttribute("message", "리뷰가 성공적으로 작성되었습니다.");
	        model.addAttribute("messageType", "success");

	        // 리뷰 목록 페이지로 리다이렉트
	        return "redirect:/review";
	    }
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
        if(loginUser != null) {
        	model.addAttribute("loginUser", loginUser);
        }
        

        return "review/reviewDetail";
	}
	
	// 리뷰 수정 화면
	@GetMapping("/edit")
    public String reviewEditView(@RequestParam("review_seq") int review_seq,
									Model model, HttpSession session) {
        Review review = reviewService.getReview(review_seq);
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

