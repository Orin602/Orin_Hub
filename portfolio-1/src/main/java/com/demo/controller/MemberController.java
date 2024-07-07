package com.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.demo.domain.Member;
import com.demo.service.MemberService;

import jakarta.servlet.http.HttpSession;


@Controller
@SessionAttributes("loginUser")
public class MemberController {

	@Autowired
	private MemberService memberService;
	
	// 로그인 화면
	@GetMapping("/login")
	public String loginView() {
		return "login/login";
	}
	
	// 로그인 처리
	@PostMapping("/login")
	public String loginAction(HttpSession session, Member vo, Model model) {
		int result = memberService.loginId(vo);
		
		model.addAttribute("message", null); // 초기화
		
		if(result == 1) {
			Member user = memberService.getMember(vo.getId());
			session.setAttribute("loginUser", user); // 세션에 로그인 사용자 정보 저장
			return "redirect:/main";
		} else if(result == -1) {
			model.addAttribute("message", "ID가 존재하지 않습니다.");
			model.addAttribute("messageType", "warning");
			
		} else {
			model.addAttribute("message", "비밀번호가 틀립니다.");
			model.addAttribute("messageType", "error");
		}
		return "login/login";
	}
	
	// 로그아웃
    @GetMapping("/logout")
    public String logout(SessionStatus status, HttpSession session) {
    	session.invalidate(); // 세션 무효화
    	status.setComplete(); // 세션 완료 상태로 설정
        return "redirect:/main"; // 로그아웃 후 초기 화면으로 이동
    }
    
    // 회원가입 화면
    @GetMapping("/join")
    public String joinView() {
    	return "login/join";
    }
    
    // 회원가입 처리
    @PostMapping("/join")
    public ResponseEntity<String> joinAction(Member vo) {
        try {
            memberService.insertMember(vo);
            return new ResponseEntity<>("회원가입이 성공적으로 완료되었습니다.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("회원가입에 실패하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // 아이디 중복확인 처리
 	@GetMapping("/id-check-form")
 	@ResponseBody
 	public Map<String, Object> idCheckAction(@RequestParam("id") String id) {
 	    int result = memberService.confirmId(id);
 	    
 	    Map<String, Object> response = new HashMap<>();
 	    response.put("result", result);
 	    
 	    return response;
 	}
    
 	// 이메일 중복확인 처리
 	@GetMapping("/email-check-form")
 	@ResponseBody
 	public Map<String, Object> emailCheckAction(@RequestParam("email") String email) {
 		int result = memberService.confirmEmail(email);
 		Map<String, Object> response = new HashMap<>();
 		response.put("result", result);
 		return response;
 	}
    // 아이디 찾기 화면
    @GetMapping("/find-id")
    public String findIdView() {
    	return "login/findId";
    }
    
    // 아이디 찾기 처리
    @PostMapping("/findidresult")
    public String findIdAction(Member vo, Model model) {
    	Member member = memberService.findId(vo.getName(), vo.getEmail());
    	if(member != null) {
    		model.addAttribute("message", 1);
    		model.addAttribute("id", member.getId());
    	} else {
    		model.addAttribute("message", -1);
    	}
    	return "login/findIdResult";
    }
    
    // 비밀번호 찾기 화면
    @GetMapping("/find-pwd")
    public String findPwdView() {
    	return "login/findPwd";
    }
    
    // 비밀번호 찾기 처리
    @PostMapping("findpwdresult")
    public String findPwdAction(Member vo, Model model) {
    	Member member = memberService.findPwd(vo.getId(), vo.getName(), vo.getEmail());
    	if(member != null) {
    		model.addAttribute("message", 1);
    		model.addAttribute("id", member.getId());
    		model.addAttribute("pwd", member.getPwd());
    	} else {
    		model.addAttribute("message", -1);
    	}
    	return "login/findPwdResult";
    }
}
