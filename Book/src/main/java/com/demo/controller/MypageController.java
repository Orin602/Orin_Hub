package com.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.demo.domain.Member;
import com.demo.domain.Qna;
import com.demo.domain.Reply;
import com.demo.domain.Review;
import com.demo.domain.ReviewInteraction;
import com.demo.domain.Store;
import com.demo.persistence.StoreRepository;
import com.demo.service.MemberService;
import com.demo.service.QnaService;
import com.demo.service.ReplyService;
import com.demo.service.ReviewInteractionService;
import com.demo.service.ReviewService;
import com.demo.service.StoreService;

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
	@Autowired
	private MemberService memberService;
	@Autowired
	private StoreService storeService;
	@Autowired
	private StoreRepository storeRepo;
	
	
	// 마이페이지
	@GetMapping("/mypage")
	public String mypageView(HttpSession session, Model model) {
		Member loginUser = (Member)session.getAttribute("loginUser");
		
		if (loginUser == null) {
			model.addAttribute("message", "로그인 필요");
			model.addAttribute("text", "리뷰 작성은 로그인 후 이용 가능합니다.");
			return "login/login";
		}
		
		model.addAttribute("loginUser", loginUser);
		
		return "mypage/mypageMain";
	}
	
	// 내가 작성한 리뷰
	@GetMapping("/myreview")
	public String myReviewView(HttpSession session, Model model) {
		Member loginUser = (Member)session.getAttribute("loginUser");
		
		if (loginUser == null) {
			model.addAttribute("message", "로그인 필요");
			model.addAttribute("text", "리뷰 작성은 로그인 후 이용 가능합니다.");
			return "login/login";
		}
		List<Review> myReview = reviewService.getByIdReview(loginUser.getId());
		
		model.addAttribute("loginUser", loginUser);
		model.addAttribute("myReview", myReview);
		
		return "mypage/myReview";
	}
	
	// 내가 추천한 리뷰
	@GetMapping("/myrecoment")
	public String myRecomentView(HttpSession session, Model model) {
		Member loginUser = (Member)session.getAttribute("loginUser");
		
		if (loginUser == null) {
			model.addAttribute("message", "로그인 필요");
			model.addAttribute("text", "리뷰 작성은 로그인 후 이용 가능합니다.");
			return "login/login";
		}
		List<ReviewInteraction> myRecoment = riService.getRecommendationsByMemberId(loginUser.getId());
		
		model.addAttribute("loginUser", loginUser);
		model.addAttribute("myRecoment", myRecoment);
		
		return "mypage/myRecoment";
	}
	
	// 내가 즐겨찾기한 리뷰
	@GetMapping("/mybookmark")
	public String myBookMarkView(HttpSession session, Model model) {
		Member loginUser = (Member)session.getAttribute("loginUser");
		
		if (loginUser == null) {
			model.addAttribute("message", "로그인 필요");
			model.addAttribute("text", "리뷰 작성은 로그인 후 이용 가능합니다.");
			return "login/login";
		}
		List<ReviewInteraction> myBookMark = riService.getBookmarksByMemberId(loginUser.getId());
		
		model.addAttribute("loginUser", loginUser);
		model.addAttribute("myBookMark", myBookMark);
		
		return "mypage/myBookMark";
	}
	// 내가 작성한 댓글
	@GetMapping("/myreply")
	public String myReplyView(HttpSession session, Model model) {
		Member loginUser = (Member)session.getAttribute("loginUser");
		
		if (loginUser == null) {
			model.addAttribute("message", "로그인 필요");
			model.addAttribute("text", "리뷰 작성은 로그인 후 이용 가능합니다.");
			return "login/login";
		}
		List<Reply> myReply = replyService.getReplyByMember(loginUser.getId());
		
		model.addAttribute("loginUser", loginUser);
		model.addAttribute("myReply", myReply);
		
		return "mypage/myReply";
	}
	
	// 내 질문 현황
	@GetMapping("/myqna")
	public String myQnaView(HttpSession session, Model model) {
		Member loginUser = (Member)session.getAttribute("loginUser");
		
		if (loginUser == null) {
			model.addAttribute("message", "로그인 필요");
			model.addAttribute("text", "리뷰 작성은 로그인 후 이용 가능합니다.");
			return "login/login";
		}
		model.addAttribute("loginUser", loginUser);
		List<Qna> myQna = qnaService.getMyQna(loginUser.getId());
		
		model.addAttribute("myQna", myQna);
		
		return "mypage/myQna";
	}
	
	// 내 구매 목록
	@GetMapping("/myorder")
	public String myOrderView(HttpSession session, Model model) {
		Member loginUser = (Member)session.getAttribute("loginUser");
		
		if (loginUser == null) {
			model.addAttribute("message", "로그인 필요");
			model.addAttribute("text", "리뷰 작성은 로그인 후 이용 가능합니다.");
			return "login/login";
		}
		model.addAttribute("loginUser", loginUser);
		List<Store> myStore = storeService.getMyOrder(loginUser.getId());
		model.addAttribute("myStore", myStore);
		
		return "mypage/myOrder";
	}
	
	// 주문 내역 수정
	@GetMapping("/edit_order")
	public String orderEditView(HttpSession session, Model model, @RequestParam("storeSeq") int storeseq) {
		Store store = storeService.getStoreBySeq(storeseq);
		model.addAttribute("store", store);
		return "mypage/editorder";
	}
	@PostMapping("/update_order")
	public ResponseEntity<String> updateOrderByUser(
	        @RequestParam("storeseq") int storeseq,
	        @RequestParam("title") String title,
	        @RequestParam("price") int price,
	        @RequestParam("ea") int ea,
	        @RequestParam("address") String address,
	        @RequestParam(value = "newAddressDetail", required = false) String newAddressDetail) {
	    
	    Store store = storeService.getStoreBySeq(storeseq);
	    store.setEA(ea);
	    
	    // 주소와 상세 주소 합치기
	    if (newAddressDetail != null && !newAddressDetail.isEmpty()) {
	        store.setAddress(address + " " + newAddressDetail);
	    } else {
	        store.setAddress(address);  // 상세 주소가 없으면 기본 주소만 저장
	    }
	    
	    store.setPrice(price);
	    storeRepo.save(store);

	    // 수정 성공 시 JSON 응답
	    return ResponseEntity.ok("주문이 성공적으로 수정되었습니다.");
	}
	// 주문내역 삭제
	@GetMapping("/delete_order")
	@ResponseBody
	public String deleteOrder(@RequestParam("storeSeq") int storeSeq) {
		storeService.deleteOrder(storeSeq);
		return "삭제 성공";
	}
	
	@PostMapping("/deleteMember")
	@ResponseBody
	public ResponseEntity<?> deleteMemberRequest(@RequestBody Map<String, String> requestData, HttpSession session) {
	    String userId = requestData.get("id");
	    String userPwd = requestData.get("pwd");

	    Member loginUser = (Member) session.getAttribute("loginUser");
	    if (loginUser == null || !loginUser.getId().equals(userId)) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access");
	    }

	    boolean isDeleted = memberService.processDeleteRequest(userId, userPwd);
	    if (isDeleted) {
	        return ResponseEntity.ok().body("탈퇴 요청 성공");
	    } else {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("아이디 또는 비밀번호가 일치하지 않습니다.");
	    }
	}
}
