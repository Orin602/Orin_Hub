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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.demo.domain.FileUploadUtil;
import com.demo.domain.Member;
import com.demo.domain.Reply;
import com.demo.domain.Review;
import com.demo.domain.ReviewInteraction;
import com.demo.domain.ReviewInteraction.InteractionType;
import com.demo.dto.ItemDTO;
import com.demo.service.MemberService;
import com.demo.service.ReplyService;
import com.demo.service.ReviewInteractionService;
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
	@Autowired
	private ReviewInteractionService reviewInteractionService;
	
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
		vo.setContent(vo.getContent().replace("\r\n", "<br>"));
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
	public ResponseEntity<String> deleteImage(@RequestParam("reviewSeq") int review_seq,
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
		review.setContent(vo.getContent().replace("\r\n", "<br>"));
		// 기존 이미지 리스트 유지
		List<String> existingImages = review.getUploadedImages();
		if (existingImages == null) {
		    existingImages = new ArrayList<>();
		}

		// 새로운 업로드된 이미지 추가
		if (vo.getUploadedImages() != null) {
		    existingImages.addAll(vo.getUploadedImages());
		}

		// 수정된 이미지 리스트를 Review 객체에 설정
		review.setUploadedImages(existingImages);
		// 파일 업로드 처리
        if (uploadFile.length > 0 || !review.getUploadedImages().isEmpty()) {
        	List<String> fileUrls = new ArrayList<>();
        	
        	if (!review.getUploadedImages().isEmpty()) {
                fileUrls.addAll(review.getUploadedImages());
            }
        	
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
            review.setUploadedImages(fileUrls); // 저장된 파일 경로를 Review 객체에 설정
        }
        
        reviewService.updateReview(review);
        return "redirect:/review";
	}

	// 리뷰 추천 처리
	@PostMapping("/review/{review_seq}/recommend")
	@ResponseBody
	public ResponseEntity<String> recommendReview(@PathVariable int review_seq, HttpSession session) {
	    Member loginUser = (Member) session.getAttribute("loginUser");
	    if (loginUser == null) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("로그인 후 이용할 수 있습니다.");
	    }

	    // 리뷰 객체 가져오기
	    Review review = reviewService.getReviewBySeq(review_seq);
	    if (review == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("리뷰를 찾을 수 없습니다.");
	    }

	    // 사용자가 이미 추천한 내역 확인
	    ReviewInteraction interaction = reviewInteractionService.findByMemberAndReviewAndInteractionType(loginUser, review, InteractionType.RECOMMENDATION);
	    
	    try {
	        if (interaction != null) {
	            // 추천이 이미 존재하므로 삭제
	            reviewInteractionService.delete(interaction);
	            reviewService.decrementRecoCount(review_seq);
	            return ResponseEntity.ok("추천이 취소되었습니다.");
	        } else {
	            // 새로운 추천 추가
	            reviewInteractionService.addInteraction(loginUser, review, InteractionType.RECOMMENDATION);
	            reviewService.incrementRecoCount(review_seq);
	            return ResponseEntity.ok("리뷰 추천이 성공적으로 처리되었습니다.");
	        }
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

	    // 리뷰 객체 가져오기
	    Review review = reviewService.getReviewBySeq(review_seq);
	    if (review == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("리뷰를 찾을 수 없습니다.");
	    }

	    // 사용자가 이미 즐겨찾기한 내역 확인
	    ReviewInteraction interaction = reviewInteractionService.findByMemberAndReviewAndInteractionType(loginUser, review, InteractionType.BOOKMARK);
	    
	    try {
	        if (interaction != null) {
	            // 즐겨찾기가 이미 존재하므로 삭제
	            reviewInteractionService.delete(interaction);
	            reviewService.decrementCheckCount(review_seq);
	            return ResponseEntity.ok("즐겨찾기가 취소되었습니다.");
	        } else {
	            // 새로운 즐겨찾기 추가
	            reviewInteractionService.addInteraction(loginUser, review, InteractionType.BOOKMARK);
	            reviewService.incrementCheckCount(review_seq);
	            return ResponseEntity.ok("리뷰가 성공적으로 즐겨찾기에 추가되었습니다.");
	        }
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("즐겨찾기 처리 중 오류가 발생했습니다.");
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
	    if (replyUser.getMember() == null || !replyUser.getMember().getId().equals(loginUser.getId())) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections.singletonMap("message", "작성자만 댓글을 삭제할 수 있습니다."));
	    }

	    // 댓글 삭제 처리
	    try {
	        replyService.deleteReply(replySeq);
	        return ResponseEntity.ok(Collections.singletonMap("message", "댓글이 성공적으로 삭제되었습니다."));
	    } catch (Exception e) {
	        e.printStackTrace();  // 에러 로그 추가
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("message", "댓글 삭제 중 오류가 발생했습니다."));
	    }
	}



	@GetMapping("/delete")
	public String deleteReview(@RequestParam("review_seq") int review_seq, HttpSession session, Model model) {
	    Member loginUser = (Member) session.getAttribute("loginUser");
	    Review review = reviewService.getReviewBySeq(review_seq);
	    
	    // 로그인 상태 확인
	    if (loginUser == null) {
	        model.addAttribute("message", "로그인 페이지로 이동");
	        model.addAttribute("text", "리뷰 삭제를 위해 로그인해주세요.");
	        model.addAttribute("messageType", "info");
	        return "login/login"; // 로그인 페이지로 이동
	    }

	    // 작성자 확인
	    if (!review.getMember().getId().equals(loginUser.getId())) {
	        model.addAttribute("message", "삭제 불가");
	        model.addAttribute("text", "작성자만 리뷰를 삭제할 수 있습니다.");
	        model.addAttribute("messageType", "error");
	        return "redirect:/review"; // 리뷰 목록 페이지로 이동
	    }
	    
	    // 리뷰 삭제 처리
	    reviewService.deleteReview(review);
	    
	    // 리뷰 삭제 후 목록으로 리다이렉트
	    return "redirect:/review"; // 리뷰 목록 페이지로 이동
	}

	@PutMapping("/update-reply")
	public ResponseEntity<String> updateReply(@RequestBody Map<String, Object> request) {
		try {
			// 요청받은 데이터 추출
			int replySeq = Integer.parseInt(request.get("replySeq").toString());
			String updatedReply = request.get("content").toString();
			
			replyService.updateReply(replySeq, updatedReply);
			
			return ResponseEntity.ok("댓글이 성공적으로 수정되었습니다."); 
		} catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
					body("댓글 수정에 실패했습니다. 다시 시도해 주세요.");
		}
	}
	
	// 도서 검색 리뷰
	@GetMapping("/review-write-form")
	public String showReviewForm(@RequestParam(required = false) String title,
								 @RequestParam(required = false) String author, 
							 	 @RequestParam(required = false) String imageUrl,
								 Model model) {
		
		model.addAttribute("bookTitle", title);
	    model.addAttribute("bookAuthor", author);
	    model.addAttribute("bookImageUrl", imageUrl);
		return "review/reviewform";
	}
	// 도서 검색 리뷰 저장
	@PostMapping("/search-review-write-action")
	public String searchReviewWrite(Review vo, Model model, HttpSession session,
			@RequestParam("coverImageUrl") String coverImageUrl) {
		Member loginUser = (Member)session.getAttribute("loginUser");
		
		if (loginUser == null) {
			model.addAttribute("message", "로그인 필요");
			model.addAttribute("text", "리뷰 작성은 로그인 후 이용 가능합니다.");
			return "login/login";
		}
		vo.setMember(loginUser);
		// 이미지 업로드 대신 bookImageUrl을 uploadedImages에 저장
	    if (coverImageUrl != null && !coverImageUrl.isEmpty()) {
	        List<String> fileUrls = new ArrayList<>();
	        fileUrls.add(coverImageUrl);  // 책 이미지 URL 추가
	        vo.setUploadedImages(fileUrls);  // Review 객체에 저장
	    }
	    vo.setContent(vo.getContent().replace("\r\n", "<br>"));
		reviewService.insertReview(vo);
		
		return "redirect:/review";
	}
	
	// 추천 도서 리뷰
	@GetMapping("/review-write-form2")
	public String recommendReviewForm(@RequestParam("bookTitle") String bookTitle,
								 @RequestParam("bookAuthor") String bookAuthor, 
							 	 @RequestParam("bookImageUrl") String bookImageUrl,
								 Model model) {
		
		model.addAttribute("bookTitle", bookTitle);
	    model.addAttribute("bookAuthor", bookAuthor);
	    model.addAttribute("bookImageUrl", bookImageUrl);
		return "review/reviewform2";
	}
	// 추천 도서 리뷰 저장
	@PostMapping("/recommend-review-write-action")
	public String recommendReviewWrite(Review vo, Model model, HttpSession session,
			@RequestParam("coverImageUrl") String coverImageUrl) {
		Member loginUser = (Member) session.getAttribute("loginUser");

		if (loginUser == null) {
			model.addAttribute("message", "로그인 필요");
			model.addAttribute("text", "리뷰 작성은 로그인 후 이용 가능합니다.");
			return "login/login";
		}
		vo.setMember(loginUser);
		// 이미지 업로드 대신 bookImageUrl을 uploadedImages에 저장
		if (coverImageUrl != null && !coverImageUrl.isEmpty()) {
			List<String> fileUrls = new ArrayList<>();
			fileUrls.add(coverImageUrl); // 책 이미지 URL 추가
			vo.setUploadedImages(fileUrls); // Review 객체에 저장
		}
		vo.setContent(vo.getContent().replace("\r\n", "<br>"));
		reviewService.insertReview(vo);

		return "redirect:/review";
	}
	@GetMapping("/review-write-form3")
	public String yes24ReviewForm(@RequestParam("bookTitle") String title, @RequestParam("bookAuthor") String author,
			@RequestParam("bookImageUrl") String image, Model model) {
		model.addAttribute("bookTitle", title);
		model.addAttribute("bookAuthor", author);
		model.addAttribute("bookImageUrl", image);
		return "review/reviewform3";
	}
	@PostMapping("/yes24-review-write-action")
	public String yes24ReviewWrite(Review vo, Model model, HttpSession session,
			@RequestParam("coverImageUrl") String coverImageUrl) {
		Member loginUser = (Member) session.getAttribute("loginUser");

		if (loginUser == null) {
			model.addAttribute("message", "로그인 필요");
			model.addAttribute("text", "리뷰 작성은 로그인 후 이용 가능합니다.");
			return "login/login";
		}
		vo.setMember(loginUser);
		// 이미지 업로드 대신 bookImageUrl을 uploadedImages에 저장
		if (coverImageUrl != null && !coverImageUrl.isEmpty()) {
			List<String> fileUrls = new ArrayList<>();
			fileUrls.add(coverImageUrl); // 책 이미지 URL 추가
			vo.setUploadedImages(fileUrls); // Review 객체에 저장
		}
		vo.setContent(vo.getContent().replace("\r\n", "<br>"));
		reviewService.insertReview(vo);

		return "redirect:/review";
	}
}


