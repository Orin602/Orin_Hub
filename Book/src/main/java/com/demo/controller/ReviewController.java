package com.demo.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.demo.domain.FileUploadUtil;
import com.demo.domain.Member;
import com.demo.domain.Reply;
import com.demo.domain.Review;
import com.demo.service.ReplyService;
import com.demo.service.ReviewService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ReviewController {

	@Autowired
	private ReviewService reviewService;
	@Autowired
	private ReplyService replyService;
	
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
			@RequestParam("uploadFile") MultipartFile[] uploadFile) {
		Member loginUser = (Member)session.getAttribute("loginUser");
		
		if(loginUser == null) {
			model.addAttribute("message", "로그인 페이지로 이동");
			model.addAttribute("text", "리뷰 작성은 로그인 후 이용 가능합니다.");
			model.addAttribute("messageType", "info");
			
			return "login/login";
		}
		vo.setMember(loginUser); // 로그인한 사용자 정보를 리뷰에 설정
		
		List<String> fileUrls = new ArrayList<>();
		for (MultipartFile file : uploadFile) {
			if (!file.isEmpty()) {
				// 파일 경로
				String uploadDir = "C:/ThisIsJava/SpringBootWorkspace/Book/uploads/";
				// 파일 이름 수정
				String originalName = file.getOriginalFilename();
				String fileExtension = originalName.substring(originalName.lastIndexOf("."));
				String uuid = UUID.randomUUID().toString();
				String fileName = uuid + fileExtension;
				
				try {
					// 파일 저장
					FileUploadUtil.saveFile(uploadDir, fileName, file);
					// URL 생성
					String fileUrl = "/uploads/" + fileName;
					fileUrls.add(fileUrl);
				} catch (IOException e) {
					e.printStackTrace();
                    model.addAttribute("message", "파일 업로드 중 오류 발생");
                    model.addAttribute("messageType", "error");
                    
                    return "review/review-write"; 
				}
				vo.setUploadedImages(fileUrls); // 저장된 파일 경로를 Review 객체에 설정
			}
		}
        reviewService.insertReview(vo);
		
		return "redirect:/review";
	}
	
	// 리뷰 상세 화면
	@GetMapping("/review_detail")
	public String reviewDetailView(HttpSession session, Model model, @RequestParam("review_seq") int review_seq) {
		Member loginUser = (Member)session.getAttribute("loginUser");
		Review review = reviewService.getReviewBySeq(review_seq);
		
		if(loginUser != null) {
        	model.addAttribute("loginUser", loginUser);
        }
		// 어보드한 이미지 파일 URL 리스트 추가
		List<String> uploadImages = review.getUploadedImages();
		model.addAttribute("uploadImages", uploadImages);
		
		// 댓글
		List<Reply> reply = replyService.getReplyByReview(review_seq);
		model.addAttribute("reply", reply);
		
		model.addAttribute("review", review);
		
		return "review/reviewDetail";
	}
	
	// 리뷰 수정 화면
	@GetMapping("/edit")
	public String reviewEditView(HttpSession session, Model model, @RequestParam("review_seq") int review_seq) {
		Member loginUser = (Member)session.getAttribute("loginUser");
		Review review = reviewService.getReviewBySeq(review_seq);
		
		if(loginUser == null) {	// 비로그인 상태
			model.addAttribute("message", "로그인 페이지로 이동");
            model.addAttribute("text", "리뷰 수정을 위해 로그인해주세요.");
            model.addAttribute("messageType", "info");
            
            return "login/login";
		}
		if(!review.getMember().getId().equals(loginUser.getId())) { // 작성자와 로그인유저 비교
			model.addAttribute("message", "수정 불가");
			model.addAttribute("text", "작성자만 리뷰를 수정할 수 있습니다.");
			model.addAttribute("messageType", "error");
			
			return "redirect:/review";
		}
		model.addAttribute("review", review);
		
		return "review/reviewEdit";
	}
	
	// 리뷰 이미지 삭제
	@GetMapping("/delete-image")
	@ResponseBody
	public ResponseEntity<String> deleteImage(@RequestParam("review_seq") int review_seq,
	        @RequestParam("imageIndex") int imageIndex) {
	    try {
	        reviewService.deleteImage(review_seq, imageIndex); // 이미지 삭제 서비스 메서드 호출
	        return ResponseEntity.ok("이미지 삭제 성공");
	    } catch(Exception e) {
	        return ResponseEntity.status(500).body("이미지 삭제 실패 : " + e.getMessage());
	    }
	}
	
	// 리뷰 수정 처리
	@PostMapping("/update-review")
	public String  reviewUpdate(HttpSession session, Model model, Review vo,
			@RequestParam("uploadFile") MultipartFile[] uploadFile,
			@RequestParam("review_seq") int review_seq) {
		Member loginUser = (Member)session.getAttribute("loginUser");
		Review review = reviewService.getReviewBySeq(review_seq);
		
		if(loginUser == null) {	// 비로그인 상태
			model.addAttribute("message", "로그인 페이지로 이동");
            model.addAttribute("text", "리뷰 수정을 위해 로그인해주세요.");
            model.addAttribute("messageType", "info");
            
            return "login/login";
		}
		if(!review.getMember().getId().equals(loginUser.getId())) { // 작성자와 로그인유저 비교
			model.addAttribute("message", "수정 불가");
			model.addAttribute("text", "작성자만 리뷰를 수정할 수 있습니다.");
			model.addAttribute("messageType", "error");
			
			return "redirect:/review";
		}
		// 제목, 내용 수정
		review.setTitle(vo.getTitle());
		review.setContent(vo.getContent());
		review.setUploadedImages(vo.getUploadedImages());
		// 파일 업로드 처리
        if (uploadFile.length > 0) {
        	List<String> fileUrls = new ArrayList<>();
            for (MultipartFile file : uploadFile) {
            	if (!file.isEmpty()) {
    				// 파일 경로
    				String uploadDir = "C:/ThisIsJava/SpringBootWorkspace/Book/uploads/";
    				// 파일 이름 수정
    				String originalName = file.getOriginalFilename();
    				String fileExtension = originalName.substring(originalName.lastIndexOf("."));
    				String uuid = UUID.randomUUID().toString();
    				String fileName = uuid + fileExtension;
    				
    				try {
    					// 파일 저장
    					FileUploadUtil.saveFile(uploadDir, fileName, file);
    					// URL 생성
    					String fileUrl = "/uploads/" + fileName;
    					fileUrls.add(fileUrl);
    				} catch (IOException e) {
    					e.printStackTrace();
                        model.addAttribute("message", "파일 업로드 중 오류 발생");
                        model.addAttribute("messageType", "error");
                        
                        return "review/reviewEdit"; 
    				}
    			}
            }
            vo.setUploadedImages(fileUrls); // 저장된 파일 경로를 Review 객체에 설정
        }
        
        reviewService.updateReview(vo);
        return "redirect:/review";
	}
	
	// 댓글 좋아요
	@PostMapping("/increment-like")
	@ResponseBody
	public ResponseEntity<String> incrementLike(@RequestParam("replySeq") int replySeq) {
		try {
			replyService.incrementLike(replySeq);
	        return ResponseEntity.ok("좋아요 증가 성공");
		} catch(Exception e) {
			return ResponseEntity.status(500).body("좋아요 증가 실패: " + e.getMessage());
		}
	}
	
	// 댓글 작성
	@PostMapping("/write-reply")
	@ResponseBody
	public ResponseEntity<String> writeReply(@RequestBody Reply reply) {
	    try {
	        // 댓글 저장 서비스 호출
	        replyService.saveReply(reply);
	        
	        // 성공 응답 반환
	        return ResponseEntity.ok("댓글이 성공적으로 작성되었습니다.");
	    } catch (Exception e) {
	        // 예외 발생 시 실패 응답 반환
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("댓글 작성 실패: " + e.getMessage());
	    }
	}
}


