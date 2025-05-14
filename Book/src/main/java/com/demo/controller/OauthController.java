package com.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.demo.domain.Member;
import com.demo.dto.User;
import com.demo.persistence.MemberRepository;
import com.demo.service.MemberService;
import com.demo.service.PasswordGenerator;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class OauthController {
	
	@Autowired
	private MemberService memberService;
    @Autowired
    private MemberRepository memberRepo;
	
	// 카카오 로그인 콜백
    @PostMapping("/kakao/callback")
    @ResponseBody
    public Map<String, String> kakaoLogin(@RequestBody User user, HttpSession session) {
    	System.out.println("Received User object: " + user);  // User 객체 출력
    	
        // 회원 조회
    	Member kakaoUser = memberService.getMember(user.getId());

        Map<String, String> response = new HashMap<>();
        
        if (kakaoUser != null) {
            session.setAttribute("loginUser", kakaoUser); // 실제 DB에서 조회한 Member 객체 저장
            response.put("status", "login_success"); // 로그인 성공 응답
        } else {
        	session.setAttribute("user", user);
        	System.out.println("User info stored in session: " + session.getAttribute("user"));
            response.put("status", "signup_required"); // 회원가입 필요 응답
        }

        return response; // JSON 형식으로 응답
    }

    // 네이버 로그인 콜백
    @GetMapping("/naver/callback")
    public String naverCallback(HttpSession session) {
    	return "login/naver";
    }
    @PostMapping("/naver/callback")
    @ResponseBody
    public Map<String, String> naverLogin(@RequestBody User user, HttpSession session) {
    	System.out.println("Naver User: " + user);
    	
    	Member naverUser = memberService.getMember(user.getId());
    	Map<String, String> response = new HashMap<>();
    	
    	if (naverUser != null) {
    		session.setAttribute("loginUser", naverUser);
    		response.put("status", "login_success");
    	} else {
    		session.setAttribute("user", user);
    		System.out.println("User info stored in session: " + session.getAttribute("user"));
    		response.put("status", "signup_required");
    	}
    	return response;
    }
    
    // 구글 로그인
    @PostMapping("/google/callback")
    public String googleLogin(@RequestParam String id, @RequestParam String name, 
            @RequestParam String email, @RequestParam String provider, HttpSession session) {
    	System.out.printf("Google User info :: { ",id, ", ", name, ", ", email, ", ", provider,"}");
    	User user = new User();
    	user.setId(id);
    	user.setName(name);
    	user.setEmail(email);
    	user.setProvider(provider);
    	session.setAttribute("loginUser", user);
    	
    	Member googleUser = memberService.getMember(user.getId());
    	if (googleUser != null) {
    		session.setAttribute("loginUser", googleUser);
    		return "redirect:/oauth/autoLogin";
    	} else {
            session.setAttribute("user", user);
            return "redirect:/oauth/contract";
        }    	
    }
    @GetMapping("/contract")
    public String contractPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        System.out.println("contract 진입 user: " + user);
        if (user != null) {
            String id = user.getId();
            int exists = memberService.confirmId(id);
            if (exists == 1) {
                return "redirect:/oauth/autoLogin";
            }
            model.addAttribute("user", user);
        }
        return "login/contract";
    }
    
    @GetMapping("/autoLogin")
    public String autoLogin(HttpSession session) {
        User user = (User) session.getAttribute("user"); 
        if (user != null) {
            String id = user.getId();
            // DB에서 해당 회원 정보를 조회
            Member loginUser = memberService.getMember(id);
            session.setAttribute("loginUser", loginUser);
            session.removeAttribute("user"); // 불필요한 user 세션 제거
        }
        return "redirect:/main";
    }
    
    @GetMapping("/joinForm")
    public String joinFormPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("name", user.getName());
            model.addAttribute("email", user.getEmail());
            model.addAttribute("id", user.getId());
            model.addAttribute("provider", user.getProvider());
        }
        return "login/joinForm";
    }
    
    @PostMapping("/confirmEmail")
    @ResponseBody
    public int confirmEmail(@RequestParam String email) {
        return memberService.confirmEmail(email);
    }
    
    @PostMapping("/joinAction")
    public String joinAction(Member vo) {
    	Member member = new Member();
        String randomPassword = PasswordGenerator.generateRandomPassword();

        member.setId(vo.getId());
        member.setName(vo.getName());
        member.setEmail(vo.getEmail());
        member.setTelephone(vo.getTelephone());
        member.setProvider(vo.getProvider());
        member.setAddress(vo.getAddress());
        member.setAddressDetail(vo.getAddressDetail());
        member.setPwd(randomPassword);
        member.setMembercode(0);

        memberRepo.save(member);
        
        return "redirect:/main";
    }
}
