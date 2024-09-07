package com.demo.controller;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.pdfbox.pdmodel.interactive.viewerpreferences.PDViewerPreferences.PRINT_SCALING;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.demo.domain.GoCamping;
import com.demo.domain.MemberData;
import com.demo.domain.Review;
import com.demo.domain.ReviewReply;
import com.demo.persistence.GoCampingRepository;
import com.demo.service.ReviewReplyService;
import com.demo.service.ReviewService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@Controller
public class ReviewController {

    @Autowired
    ReviewService reviewsv;
    
    @Autowired
    ReviewReplyService replysv;
    
    @Autowired
    GoCampingRepository gocampingRepo;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    private final String uploads = Paths.get(System.getProperty("user.dir"), "uploads").toString();  
    
    //큰메뉴 캠핑장검색페이지로
    @GetMapping("/map")
    public String go_mapsearch() {
        return "Mapsearch/regionMapApi";
    }
    
    // 리뷰작성시 지도로 찾기
    @GetMapping("/kakao-map")
    public String kakaoMap() {
        return "Community/search_kakaomap";
    }
    
    //리뷰작성시 이름으로찾기
    @GetMapping("/gocampreview")
    public String gocamping_serch() {
        return "Community/search_gocamp";
    }
    
    //리뷰 리스트
    @GetMapping(value = {"/review", "/review_list"})
    public String getReviewList(@RequestParam(value = "review_seq", defaultValue = "1") int review_seq,
                                @RequestParam(value = "page", defaultValue = "1") int page,
                                @RequestParam(value = "size", defaultValue = "6") int size, Model model) {
        Page<Review> pageList = reviewsv.getAllReview(review_seq, page, size);
        List<Review> reviewList = pageList.getContent();
        
        long totalElements = pageList.getTotalElements();
        long startNumber = totalElements - (page - 1) * size;

        model.addAttribute("reviewList", reviewList);
        model.addAttribute("totalPages", pageList.getTotalPages());
        model.addAttribute("pageNumber", page);
        model.addAttribute("startNumber", startNumber);

        return "Community/ReviewList";
    }
    
    //맵통해서 리뷰페이지로
    @GetMapping("/map_review_detail")
    public String getReviewDetail(@RequestParam(value = "kakao_id") String kakao_id,
                                  Model model,
                                  @RequestParam(value = "review_seq", defaultValue = "1") int review_seq,
                                  @RequestParam(value = "page", defaultValue = "1") int page,
                                  @RequestParam(value = "size", defaultValue = "6") int size) {
    	


        List<Review> reviewList = reviewsv.getReviewsByKakaoId(kakao_id);
        Page<Review> pageList = reviewsv.getReviewBykakao_id(review_seq, page, size, kakao_id);
        reviewList.sort(Comparator.comparingInt(Review::getReview_seq));
        long totalElements = pageList.getTotalElements();
        long startNumber = totalElements - (page - 1) * size;
       

        model.addAttribute("reviewList", reviewList);
        model.addAttribute("totalPages", pageList.getTotalPages());
        model.addAttribute("pageNumber", page);
        model.addAttribute("startNumber", startNumber);
        
        return "Community/ReviewList";
    }
    
  //추천통해서 리뷰페이지로
    @GetMapping("/gocamping_review_detail")
    public String getgocampingReviewDetail(@RequestParam(value = "campid") String kakao_id,
                                  Model model,
                                  @RequestParam(value = "review_seq", defaultValue = "1") int review_seq,
                                  @RequestParam(value = "page", defaultValue = "1") int page,
                                  @RequestParam(value = "size", defaultValue = "6") int size) {
    	


        List<Review> reviewList = reviewsv.getReviewsByKakaoId(kakao_id);
        Page<Review> pageList = reviewsv.getReviewBykakao_id(review_seq, page, size, kakao_id);
        reviewList.sort(Comparator.comparingInt(Review::getReview_seq));
        long totalElements = pageList.getTotalElements();
        long startNumber = totalElements - (page - 1) * size;
       

        model.addAttribute("reviewList", reviewList);
        model.addAttribute("totalPages", pageList.getTotalPages());
        model.addAttribute("pageNumber", page);
        model.addAttribute("startNumber", startNumber);
        
        return "Community/ReviewList";
    }
    
    
	// 게시글 상세정보 조회
	@GetMapping("/review_detail")
	public String getCom_Board_DetailView(@RequestParam("review_seq") int review_seq, Model model, HttpSession session) {
		Review vo = reviewsv.getReview(review_seq);
		Set<Integer> viewed = (Set<Integer>) session.getAttribute("viewed");
		
		
		 if (viewed == null) { // 처음클릭시
		        viewed = new HashSet<>();
		        session.setAttribute("viewed", viewed);
		    }

		    if (!viewed.contains(review_seq)) {
		        if (vo != null) {
		            vo.setCnt(vo.getCnt() + 1);  // 조회수 1 증가
		            reviewsv.updateReview(vo);  // DB에 업데이트
		        }
		        viewed.add(review_seq); //방문한 게시글번호 추가
		    }
	    
		model.addAttribute("reviewVO", vo);

		return "Community/ReviewDetail";
	}


	// 리뷰 쓰기
    @GetMapping("/reviewwrite")
    public String getReviewWriteView(HttpSession session) {
    	MemberData loginUser = (MemberData) session.getAttribute("loginUser");
    	if (loginUser == null) {
    		return "loginForm";
        }
    
        return "Community/ReviewWrite";
    }

    // 리뷰 글 전송
    @Transactional
    @PostMapping("/reviewwritesubmit")
    public ResponseEntity<String> insertReview(HttpSession session, @RequestBody Review review) {
        MemberData loginUser = (MemberData) session.getAttribute("loginUser");
        
        loginUser = entityManager.merge(loginUser);
        review.setMember_data(loginUser);
        reviewsv.insertReview(review);
        
        return new ResponseEntity<>("Review submitted successfully", HttpStatus.OK);
    }
    

    // 이미지 업로드
    @PostMapping("/tui-editor/image-upload")
    public ResponseEntity<String> uploadEditorImage(@RequestParam("image") MultipartFile image) {
        if (image.isEmpty()) {
            return new ResponseEntity<>("Empty file.", HttpStatus.BAD_REQUEST);
        }

        String orgFilename = image.getOriginalFilename();
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String extension = orgFilename.substring(orgFilename.lastIndexOf(".") + 1);
        String saveFilename = uuid + "." + extension;
        String fileFullPath = Paths.get(uploads, saveFilename).toString();

        File dir = new File(uploads);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try {
            File uploadFile = new File(fileFullPath);
            image.transferTo(uploadFile);
            String imageUrl = "/uploads/" + saveFilename; // 정적 리소스 URL 반환
            return new ResponseEntity<>(imageUrl, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("File upload error.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    
	// 제목, 작성자, 캠핑장명으로 검색
	@GetMapping("/review_search")
	public String getSearchByType(@RequestParam(value = "seq", defaultValue = "1") int seq,
			@RequestParam("searchType") String searchType, @RequestParam("searchKeyword") String keyword,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "size", defaultValue = "6") int size, Model model) {

		Page<Review> pageList;

		if ("title".equals(searchType)) {
	        pageList = reviewsv.getReviewByTitle(seq, page, size, keyword);
	    } else if ("Kamping".equals(searchType)) {
	        pageList = reviewsv.getReviewByCamping(seq, page, size, keyword);
	    } else {
	        pageList = reviewsv.getReviewByWriter(seq, page, size, keyword);
	    }

	    List<Review> searchResult = pageList.getContent();
	    long totalElements = pageList.getTotalElements();
	    long startNumber = totalElements - (page - 1) * size;

	    model.addAttribute("reviewList", searchResult);
	    model.addAttribute("totalPages", pageList.getTotalPages());
	    model.addAttribute("pageNumber", page);
	    model.addAttribute("startNumber", startNumber);

		return "Community/ReviewList";
	}
	
	
	// 고캠핑 캠핑장찾기
	@GetMapping("/campingname_search")
	public String getGocampingSearch(@RequestParam(value = "content_id", defaultValue = "1") int content_id,
			@RequestParam("searchKeyword") String keyword,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "size", defaultValue = "10") int size, Model model) {
		
		Pageable pageable = PageRequest.of(page - 1, size, Direction.ASC, "content_id");
		Page<GoCamping> pageList = gocampingRepo.searchreview_List(keyword, content_id, pageable);

	    List<GoCamping> searchResult = pageList.getContent();
	    long totalElements = pageList.getTotalElements();
	    long startNumber = totalElements - (page - 1) * size;

	    model.addAttribute("reviewList", searchResult);
	    model.addAttribute("totalPages", pageList.getTotalPages());
	    model.addAttribute("pageNumber", page);
	    model.addAttribute("startNumber", startNumber);

		return "Community/search_gocamp";
	}
	
	
	// 글수정 페이지로 이동
	@GetMapping("/review_update")
	public String getReviewUpdate(HttpSession session, Model model, @RequestParam("review_seq") int review_seq) {
		MemberData loginUser =  (MemberData)session.getAttribute("loginUser");
		Review reviewVO = reviewsv.getReview(review_seq);

			if (loginUser == null) { 
				return "loginForm"; 
			} else if(!(loginUser.getId()).equals(reviewVO.getMember_data().getId())){
				return "본인이 작성한 글만 수정가능합니다.";
			}else {
				
		        model.addAttribute("reviewVO", reviewVO);
		        }
				
				return "Community/ReviewUpdate";
			
	}
	
	
	//글 수정
	@Transactional
	@PostMapping("/reviewupdatesubmit")
	public ResponseEntity<String> updateReview(HttpSession session, @RequestBody Review review) {
		MemberData loginUser = (MemberData) session.getAttribute("loginUser");
		Review reviewVO = reviewsv.getReview(review.getReview_seq());
		
		reviewVO.setContent(review.getContent());
		reviewVO.setImages(review.getImages());
		reviewVO.setKakao_name(review.getKakao_name());
		reviewVO.setKakao_id(review.getKakao_id());
		reviewVO.setTitle(review.getTitle());
		reviewVO.setReviewrate(review.getReviewrate());
        reviewsv.updateReview(reviewVO);
        
        return new ResponseEntity<>("Review submitted successfully", HttpStatus.OK);
        
	}

	
	// 글 삭제
	@GetMapping("/review_delete")
	public String deleteReview(@RequestParam(value = "review_seq") int review_seq, HttpSession session) {
		MemberData loginUser = (MemberData) session.getAttribute("loginUser");
		Review vo = reviewsv.getReview(review_seq);

		if (loginUser == null) { 
			return "loginForm"; 
		}else if(!(loginUser.getId()).equals(vo.getMember_data().getId())){
			return "본인이 작성한 글만 삭제가능합니다.";
		}else {	
		reviewsv.deleteReview(vo);
		return "redirect:/review";
	}
	}
	
	//추천수 관리
	@PostMapping("/goodpoint")
	public String goodPoint_Action(@RequestParam("review_seq") int review_seq, HttpSession session) {
		Review vo = reviewsv.getReview(review_seq);
	    HashMap<Integer, String> goodPointStatusMap = (HashMap<Integer, String>) session.getAttribute("goodPointStatusMap");

	    if (goodPointStatusMap == null) {
	        goodPointStatusMap = new HashMap<>();
	        session.setAttribute("goodPointStatusMap", goodPointStatusMap);
	    }

	    String goodPointStatus = goodPointStatusMap.get(review_seq);

	    if (goodPointStatus == null || goodPointStatus.equals("off")) {
	        if (vo != null) {
	            vo.setGoodpoint(vo.getGoodpoint() + 1);
	            reviewsv.updateReview(vo);
	            goodPointStatusMap.put(review_seq, "on");
	        }
	    } else if (goodPointStatus.equals("on")) {
	        if (vo != null) {
	            vo.setGoodpoint(vo.getGoodpoint() - 1);
	            reviewsv.updateReview(vo);
	            goodPointStatusMap.put(review_seq, "off");
	        }
		    }
		    
		return "redirect:/review_detail?review_seq=" + review_seq;
	}
	
	//북마크 관리
	@PostMapping("/bookmark")
	public String bookMark_Action(@RequestParam("review_seq") int review_seq, HttpSession session) {
		MemberData loginUser = (MemberData) session.getAttribute("loginUser");
		if (loginUser == null) { 
			return "loginForm"; 
		}else {
		Review vo = reviewsv.getReview(review_seq);
	    HashMap<Integer, String> bookmarkStatusMap = (HashMap<Integer, String>) session.getAttribute("bookmarkStatusMap");

	    if (bookmarkStatusMap == null) {
	    	bookmarkStatusMap = new HashMap<>();
	        session.setAttribute("bookmarkStatusMap", bookmarkStatusMap);
	    }

	    String bookmarkStatus = bookmarkStatusMap.get(review_seq);

	    if (bookmarkStatus == null || bookmarkStatus.equals("off")) {
	        if (vo != null) {
	            vo.setBookmark(vo.getBookmark() + 1);
	            reviewsv.updateReview(vo);
	            bookmarkStatusMap.put(review_seq, "on");
	        }
	    } else if (bookmarkStatus.equals("on")) {
	        if (vo != null) {
	        	vo.setBookmark(vo.getBookmark() - 1);
	            reviewsv.updateReview(vo);
	            bookmarkStatusMap.put(review_seq, "off");
	        }
		    }
		}
		return "redirect:/review_detail?review_seq=" + review_seq;
	}
	
	
	
	//인기도 정렬
	 @GetMapping(value = "/sorted_Review", produces = MediaType.TEXT_HTML_VALUE)
	    public String getSortedReviewListHtml(@RequestParam(value = "review_seq", defaultValue = "1") int review_seq,
	                                         @RequestParam(value = "page", defaultValue = "1") int page,
	                                         @RequestParam(value = "size", defaultValue = "6") int size,
	                                         @RequestParam("sort") String sort, Model model) {

	        Page<Review> pageList = null;

	        switch (sort) {
	            case "cnt_sort":
	                pageList = reviewsv.getCReviewByCnt(review_seq, page, size);
	                break;
	            case "goodpoint_sort":
	                pageList = reviewsv.getReviewByGoodpoint(review_seq, page, size);
	                break;
	            case "bookmark_sort":
	                pageList = reviewsv.getReviewByBookmark(review_seq, page, size);
	                break;    
	        }

	        List<Review> reviewList = pageList.getContent();
	        long totalElements = pageList.getTotalElements();
	        long startNumber = totalElements - (page - 1) * size;

	        
	        model.addAttribute("reviewList", reviewList);
	        model.addAttribute("totalPages", pageList.getTotalPages());
		    model.addAttribute("pageNumber", page);
		    model.addAttribute("startNumber", startNumber);
	        
	        List<Review> top3Cnt = new ArrayList<>();
	        List<Review> top3Goodpoint = new ArrayList<>();
	        
	        if (sort.equals("cnt_sort")) {
	            top3Cnt = reviewList.stream().sorted((b1, b2) -> Integer.compare(b2.getCnt(), b1.getCnt())).limit(3).collect(Collectors.toList());
	            model.addAttribute("top3Cnt", top3Cnt);
	        } else if (sort.equals("goodpoint_sort")) {
	            top3Goodpoint = reviewList.stream().sorted((b1, b2) -> Integer.compare(b2.getGoodpoint(), b1.getGoodpoint())).limit(3).collect(Collectors.toList());
	            model.addAttribute("top3Goodpoint", top3Goodpoint);
	        }

	        return "Community/ReviewList";
	    }
	 
	 
		/* 여기서 부터 댓글 */
		/*
		 * */
	 
		//댓글 출력
		@PostMapping("/reply_list")
		public String getReply_list(@RequestParam("review_seq") int review_seq, Model model) {
			
			
			List<ReviewReply> ReplyList = replysv.getReplyBySeq(review_seq);
			Review ReviewVO = reviewsv.getReview(review_seq);
			
			model.addAttribute("ReplyList", ReplyList);
			model.addAttribute("reviewVO", ReviewVO);
			
			
			return "Community/ReviewDetail";
			}
		
		
		
		
		//댓글 등록
		@PostMapping("/reply_save")
		public String insertReply(@RequestParam("review_seq") int review_seq, HttpSession session,
				@RequestParam("reply_content") String reply_content) {
			
			MemberData loginUser = (MemberData) session.getAttribute("loginUser");
			Review review = reviewsv.getReview(review_seq);
			ReviewReply reply = new ReviewReply();
			
			if (loginUser == null) { 
				return "loginForm"; 
			}else {
				reply.setReview(review);
				reply.setMember_data(loginUser);
				reply.setContent(reply_content);
				
				replysv.insertReply(reply);
				return "redirect:/review_detail?review_seq=" + review_seq;
				}
		}
		
		//***** 댓글 수정 *****
		@PostMapping(value = "/reply_update")
		@ResponseBody
		public Map<String, Object> updateReply(@RequestBody Map<String, Object> payload, HttpSession session) {
		    Map<String, Object> response = new HashMap<>();
		    try {
		        int replynum = Integer.parseInt(payload.get("replynum").toString());
		        String content = payload.get("content").toString();
		        MemberData loginUser = (MemberData) session.getAttribute("loginUser");
		        ReviewReply reply = replysv.findReplyByreplynum(replynum);

		        if (loginUser == null || !loginUser.getId().equals(reply.getMember_data().getId())) {
		            response.put("success", false);
		            response.put("message", "본인이 작성한 댓글만 수정 가능합니다.");
		            return response;
		        }

		        reply.setContent(content);
		        replysv.updateReply(reply);
		        response.put("success", true);
		        response.put("message", "댓글이 수정되었습니다.");
		    } catch (Exception e) {
		        e.printStackTrace();
		        response.put("success", false);
		        response.put("message", "서버 오류가 발생했습니다. 오류 메시지: " + e.getMessage());
		    }
		    return response;
		}

		
		//댓글 삭제 
		@PostMapping("/reply_delete")
		@ResponseBody
		public Map<String, Object> deleteReply(@RequestParam("replynum") int replynum, HttpSession session) {
		    Map<String, Object> response = new HashMap<>();
		    try {
		        MemberData loginUser = (MemberData) session.getAttribute("loginUser");
		        ReviewReply reply = replysv.findReplyByreplynum(replynum);

		        if (loginUser == null) {
		            response.put("success", false);
		            response.put("message", "로그인이 필요합니다.");
		            return response;
		        }

		        if (!loginUser.getId().equals(reply.getMember_data().getId())) {
		            response.put("success", false);
		            response.put("message", "본인이 작성한 댓글만 삭제 가능합니다.");
		            return response;
		        }

		        replysv.deleteReply(reply);
		        response.put("success", true);
		        response.put("message", "댓글이 삭제되었습니다.");
		    } catch (Exception e) {
		        response.put("success", false);
		        response.put("message", "서버 오류가 발생했습니다.");
		    }
		    return response;
		}
		

}
