package com.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.demo.domain.Member;
import com.demo.service.MemberService;

import jakarta.servlet.http.HttpSession;

@Controller
@SessionAttributes("admin")
public class AdminContoller {

	@Autowired
	private MemberService memberService;
	
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
}
