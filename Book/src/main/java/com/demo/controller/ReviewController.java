package com.demo.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.demo.domain.FileUploadUtil;
import com.demo.domain.Member;
import com.demo.domain.Reply;
import com.demo.domain.Review;
import com.demo.dto.ItemDTO;
import com.demo.service.MemberService;
import com.demo.service.ReplyService;
import com.demo.service.ReviewService;
import com.demo.service.SearchService;

import jakarta.servlet.http.HttpSession;



@Controller
public class ReviewController {

	@Autowired
	private ReviewService reviewService;
	@Autowired
	private ReplyService replyService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private SearchService searchService;
	
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
	public String reviewDetailView(HttpSession session, Model model, @RequestParam("review_seq") int review_seq) throws Exception {
		Member loginUser = (Member)session.getAttribute("loginUser");
		Review review = reviewService.getReviewBySeq(review_seq);
		 // 사용자가 이 리뷰를 이미 조회했는지 확인 (세션에 저장된 리스트로 확인)
	    List<Integer> viewedReviews = (List<Integer>) session.getAttribute("viewedReviews");
	    if (viewedReviews == null) {
	        viewedReviews = new ArrayList<>();
	    }
		if(loginUser != null) {
        	model.addAttribute("loginUser", loginUser);
        	// 세션에 해당 리뷰가 없으면 조회수 증가
            if (!viewedReviews.contains(review_seq)) {
                reviewService.incrementViewCount(review_seq);
                viewedReviews.add(review_seq);
                session.setAttribute("viewedReviews", viewedReviews); // 세션에 추가
            }
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

	// 리뷰 추천 처리
	@PostMapping("/review/{review_seq}/recommend")
	@ResponseBody
	public ResponseEntity<String> recommendReview(@PathVariable int review_seq, HttpSession session) {
		Member loginUser = (Member)session.getAttribute("loginUser");
		if(loginUser == null) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("로그인 후 이용할 수 있습니다.");
			
		}
		try {
	        // 추천 처리
			reviewService.incrementRecoCount(review_seq);
	        return ResponseEntity.ok("리뷰 추천이 성공적으로 처리되었습니다.");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("리뷰 추천 처리 중 오류가 발생했습니다.");
	    }
	}
	
	// 리뷰 즐겨찾기 처리
	@PostMapping("/review/{review_seq}/bookmark")
	@ResponseBody
	public ResponseEntity<String> bookmarkReview(@PathVariable int review_seq, HttpSession session) {
	    Member loginUser = (Member) session.getAttribute("loginUser");
	    if (loginUser == null) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("로그인 후 이용할 수 있습니다.");
	    }

	    try {
	        // 즐겨찾기 처리
	        reviewService.incrementCheckCount(review_seq);

	        return ResponseEntity.ok("리뷰 즐겨찾기가 성공적으로 처리되었습니다.");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("리뷰 즐겨찾기 처리 중 오류가 발생했습니다.");
	    }
	}
	
	// 댓글 작성
	@PostMapping("/write-reply")
	@ResponseBody
	public ResponseEntity<String> writeReply(@RequestBody Map<String, Object> requestBody) {
	    try {
	        // requestBody에서 review_seq와 member_id 추출
	        int reviewSeq = Integer.parseInt((String) requestBody.get("review_seq"));
	        String memberId = (String) requestBody.get("member_id");
	        String content = (String) requestBody.get("content");

	        // Review 객체와 Member 객체를 데이터베이스에서 조회
	        Review review = reviewService.getReviewBySeq(reviewSeq);
	        Member member = memberService.getMember(memberId);

	        // Reply 객체 생성
	        Reply reply = Reply.builder()
	                            .content(content)
	                            .review(review)
	                            .member(member)
	                            .reply_date(new Date()) // 현재 날짜 및 시간 설정
	                            .build();

	        // 댓글 저장 서비스 호출
	        replyService.saveReply(reply);
	        
	        return ResponseEntity.ok("댓글이 성공적으로 작성되었습니다.");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("댓글 작성 실패: " + e.getMessage());
	    }
	}

	@GetMapping("/book-review/{title}")
	public String writeReview(@PathVariable("title") String title, Model model, HttpSession session) {
	    // 세션에서 검색 결과 가져오기
	    List<ItemDTO> items = (List<ItemDTO>) session.getAttribute("searchResults");
	 
	    // 해당 책 정보를 찾기
	    ItemDTO selectedItem = items.stream()
	                                .filter(item -> item.getTitle().equals(title))
	                                .findFirst()
	                                .orElse(null);

	    if (selectedItem != null) {
	        model.addAttribute("item", selectedItem);
	        model.addAttribute("review", new Review());
	        return "review/reviewWrite";
	    } else {
	        model.addAttribute("message", "책 정보를 가져오는 데 실패했습니다.");
	        return "error"; // 적절한 오류 페이지로 리다이렉션
	    }
	}
	
	// 댓글 좋아요 처리
	@PostMapping("/replies/{replySeq}/like")
    @ResponseBody
    public ResponseEntity<String> likeReply(@PathVariable int replySeq, HttpSession session) {
        // 로그인한 사용자 확인
        Member loginUser = (Member) session.getAttribute("loginUser");

        if (loginUser == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("로그인 후 이용할 수 있습니다.");
        }

        String userId = loginUser.getId();
        
        try {
            // 좋아요가 추가되었는지 취소되었는지 확인
            boolean liked = replyService.toggleLike(userId, replySeq);

            if (liked) {
                return ResponseEntity.ok("좋아요가 성공적으로 추가되었습니다.");
            } else {
                return ResponseEntity.ok("좋아요가 취소되었습니다.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("좋아요 처리 중 오류가 발생했습니다.");
        }
    }
	
	// 댓글 삭제 처리
	@PostMapping("/replies/{replySeq}/delete")
	public ResponseEntity<Map<String, String>> deleteReply(@PathVariable int replySeq, HttpSession session) {
	    // 로그인한 사용자 확인
	    Member loginUser = (Member) session.getAttribute("loginUser");

	    if (loginUser == null) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections.singletonMap("message", "로그인 후 이용할 수 있습니다."));
	    }

	    // 댓글 정보 가져오기
	    Reply replyUser = replyService.getReplyBySeq(replySeq);
	    if (replyUser == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", "해당 댓글을 찾을 수 없습니다."));
	    }

	    // 댓글 작성자와 로그인한 사용자 확인
	    if (!replyUser.getMember().getId().equals(loginUser.getId())) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections.singletonMap("message", "작성자만 댓글을 삭제할 수 있습니다."));
	    }

	    // 댓글 삭제 처리
	    try {
	        replyService.deleteReply(replySeq);
	        return ResponseEntity.ok(Collections.singletonMap("message", "댓글이 성공적으로 삭제되었습니다."));
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("message", "댓글 삭제 중 오류가 발생했습니다."));
	    }
	}

}


