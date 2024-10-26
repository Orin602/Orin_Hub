package com.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.demo.domain.Member;
import com.demo.domain.Notice;
import com.demo.service.MemberService;
import com.demo.service.NoticeService;

import jakarta.servlet.http.HttpSession;

@Controller
@SessionAttributes("admin")
public class AdminContoller {

	@Autowired
	private MemberService memberService;
	@Autowired
	private NoticeService noticeService;
	
	@GetMapping("/admin")
	public String adminLogin() {
		return "admin/admin_login";
	}
	
	@PostMapping("/admin_ok")
	public String adminMainView(HttpSession session, Member vo, Model model) {
		int result = memberService.adminId(vo);
		model.addAttribute("message", null); // 초기화
		
		if(result == 1) {
			Member admin = memberService.getMember(vo.getId());
			session.setAttribute("admin", admin);	// 세션에 로그인 사용자 정보 저장
			return "admin/adminMain";
		} else {
	        // 로그인 실패 처리
	        if(result == 2) {
	            model.addAttribute("message", "관리자 권한이 없습니다.");
	            model.addAttribute("messageType", "warning");
	        } else if(result == -1) {
	            model.addAttribute("message", "비밀번호가 틀립니다.");
	            model.addAttribute("messageType", "error");
	        } else {
	            model.addAttribute("message", "ID가 존재하지 않습니다.");
	            model.addAttribute("messageType", "warning");
	        }
		}
		return "admin/admin_login";
	}
	
	@GetMapping("/admin-logout")
	public String adminLogout(HttpSession session) {
		session.invalidate(); // 현재 세션을 무효화
		return "main";
	}
	
	@GetMapping("/admin-notice-list")
	public String allNoticeList(HttpSession session, Model model) {
	    List<Notice> notices = noticeService.getAllNotices();	// 모든 공지사항
	    
	    model.addAttribute("notices", notices);
	    return "admin/section/notice_list"; // 뷰 이름
	}

	@GetMapping("/admin-notice-write")
	public String writeNotice(HttpSession session, Model model) {
	    // 공지사항 작성 폼을 보여주는 로직 구현
	    return "admin/section/notice_write"; // 뷰 이름
	}

	@GetMapping("/admin-notice-ED")
	public String editDeleteNotice(HttpSession session, Model model) {
	    // 공지사항 수정/삭제 폼을 보여주는 로직 구현
	    return "admin/section/notice_edit_delete"; // 뷰 이름
	}

	@GetMapping("/admin-customer-list")
	public String allCustomerList(HttpSession session, Model model) {
	    // 회원 목록을 가져오는 로직 구현
	    return "admin/section/customer_list"; // 뷰 이름
	}

	@GetMapping("/admin-fix-qna")
	public String fixedQuestions(HttpSession session, Model model) {
	    // 고정 질문 목록을 가져오는 로직 구현
	    return "admin/section/fixed_questions"; // 뷰 이름
	}

	@GetMapping("/admin-customer-qna")
	public String customerQuestions(HttpSession session, Model model) {
	    // 회원 질문 목록을 가져오는 로직 구현
	    return "admin/section/customer_questions"; // 뷰 이름
	}

	@GetMapping("/admin-review")
	public String reviewList(HttpSession session, Model model) {
	    // 리뷰 목록을 가져오는 로직 구현
	    return "admin/section/review_list"; // 뷰 이름
	}

	@GetMapping("/admin-apply")
	public String commentList(HttpSession session, Model model) {
	    // 댓글 목록을 가져오는 로직 구현
	    return "admin/section/comment_list"; // 뷰 이름
	}

}
