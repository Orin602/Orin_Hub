package com.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.demo.domain.MemberData;
import com.demo.service.MemberService;

@Controller
@SessionAttributes("loginUser")
public class MemberController {

    @Autowired
    private MemberService memberService;

 // 로그인 화면 표시
    @GetMapping("/login")
    
    public String loginView() {
        return "member/login";
    }
    
    // 약정화면 표시
    @GetMapping("/contract")
    public String contractView() {
    	
        return "member/contract";
    }
    
    // 회원가입 페이지로 이동
    @PostMapping("/join_form")
    public String joinView() {
    	return "member/join";
    }
   
    @PostMapping("/contract")
    public String signUp(@RequestParam(name = "agree", required = false) String agree) {
        if ("on".equals(agree)) { // 약관에 동의한 경우
            return "member/join"; // 회원가입 페이지로 이동
        } else {
            return "member/contract"; // 약관 동의 페이지로 다시 이동
        }
    } 
    
 // 아이디찾기 화면
    @GetMapping("/findId")
    public String findIdAndPasswordView() {
        return "member/findId";
    }
    // 비밀번호 찾기 화면
    @GetMapping("/findPassword")
    public String findPassword() {
       return "member/findPassword";
    }
  
 // 아이디 찾기 처리
  	@PostMapping("/find_id")
  	public String findIdAction(MemberData vo, Model model) {
  		
  		MemberData member = memberService.getIdByNameEmail(vo.getName(), vo.getEmail());
  		
  		System.out.println("member=" + member);
  		if (member != null) { //아이디 조회 성공
  			model.addAttribute("message", 1);
  			model.addAttribute("id", member.getId());
  		} else {
  			model.addAttribute("message", -1);
  		}
  		
  		return "member/findResult";
  	}
  	
  	
 	// 비밀번호 찾기
 	@PostMapping("/find_pwd")
 	public String findPwdAction(MemberData vo, Model model) {
 		
 		MemberData member = memberService.getPwdByIdNameEmail(vo.getId(), vo.getName(), vo.getEmail());
 		
 		if (member != null) { // 사용자 조회 성공
 			model.addAttribute("message", 1);
 			model.addAttribute("id", member.getId());
 			model.addAttribute("password", member.getPassword());
 		} else {
 			model.addAttribute("message", -1);
 		}
 		
 		return "member/findPwdResult";
 	}
  	
  	
    
    // 로그인
    @PostMapping("/login")
    public String loginAction(MemberData vo, Model model) {
    	int result = memberService.loginID(vo);
        String url = "";        
        if (result == 1) { 
        	MemberData user = memberService.getMember(vo.getId());
        	long usercode = user.getUsercode();
        	if(usercode == 1) {
        		model.addAttribute("loginUser", user);
                url = "redirect:main.do";
        	} else {
        		model.addAttribute("loginUser", user);
                url = "redirect:/";
        	}
            
        } else {
            url = "member/login_fail";
        }
        return url;
    }

    // 로그아웃 처리
    @GetMapping("/logout")
    public String logout(SessionStatus status) {
        status.setComplete(); // 세션 완료 상태로 설정
        return "member/login"; // 로그아웃 후 로그인 화면으로 리다이렉트
    }
    
   
    // ID 중복 확인 처리
    @GetMapping("/id_check_form")
    public String idCheckView(@RequestParam("id")String id, Model model) {
        int result = memberService.confirmID(id);
        
        model.addAttribute("message", result);
        model.addAttribute("id", id);
        
        return "member/idcheck";
    }

    // 이메일 중복 확인 처리
    @GetMapping("/email_check_form")
    public String emailCheckView(@RequestParam("email") String email, Model model) {
        int result = memberService.confirmEmail(email);
        model.addAttribute("message", result);
        model.addAttribute("email", email);
        return "member/emailcheck";
    }
    
    // 회원가입 처리 (POST 요청)
    @PostMapping("/join")
    public String joinAction(MemberData vo) {
        memberService.insertMember(vo);
        return "redirect:/"; // 회원가입 후 메인 페이지로 리다이렉트
    }
    
    
        
}