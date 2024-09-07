package com.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.demo.domain.MemberData;
import com.demo.service.MemberService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class UserController {

	@Autowired
	MemberService memberService;
	
    @GetMapping("oauth/login")
    public String loginPOSTNaver(HttpSession session) {
        log.info("callback controller");
        return "member/callback";
    }
    
    @PostMapping("/oauth/join_naver")
    public String joinNaverAction(MemberData vo) {
        memberService.insertMemberNaver(vo);
        
        return "member/resultEnd";
    }
    
    @PostMapping("/checkUser")
    @ResponseBody
    public Map<String, Object> checkUser(@RequestParam String id) {
        Map<String, Object> response = new HashMap<>();
        
        // 사용자 정보를 데이터베이스에서 조회하여 존재 여부 확인
        int exists = memberService.confirmID(id); // 예시: userService에서 사용자 존재 여부 확인
        
        response.put("exists", exists);
        return response;
    }
    
    @GetMapping("oauth/autologin")
    public String autologin(@RequestParam String id, Model model, HttpSession session) {
    	MemberData loginUser = memberService.getMember(id);
    	
    	session.setAttribute("loginUser", loginUser);
    	
        return "redirect:/"; // 로그인 후 메인 페이지로 리다이렉트
    }
    
}