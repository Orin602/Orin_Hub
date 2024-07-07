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

import com.demo.domain.Member;
import com.demo.service.MemberService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MypageController {

	@Autowired
    private MemberService memberService;
	
	// 마이페이지 화면
	@GetMapping("/mypage")
	public String mypageView() {
		return "mypage/mypage";
	}
	
	// 내 정보 화면
	@GetMapping("/infoView")
	public String infoView(HttpSession session, Model model) {
		Member loginUser = (Member)session.getAttribute("loginUser");
		
		if(loginUser == null) {
			model.addAttribute("message", "로그인 후 이용 가능합니다.");
			model.addAttribute("messageType", "info");
			return "redirect:/login";
		}
		Member userInfo = memberService.getMember(loginUser.getId());
		
		model.addAttribute("nickname", userInfo.getNickname());
		model.addAttribute("cnt", "내가 작성한 글 수");
		
		return "mypage/infoView";
	}
	
	// 내 정보 수정 화면
	@GetMapping("/infoUpdate")
	public String infoUpdateView(HttpSession session, Model model) {
		Member loginUser = (Member)session.getAttribute("loginUser");
		
		if(loginUser == null) {
			model.addAttribute("message", "로그인 후 이용 가능합니다.");
			model.addAttribute("messageType", "info");
			return "redirect:/login";
		}
		Member userInfo = memberService.getMember(loginUser.getId());
		
		model.addAttribute("loginUser", userInfo);
		
		return "mypage/infoUpdate";
	}
	
	// 내 정보 수정 처리
	@PostMapping("/updateInfo")
	@ResponseBody
	public Map<String, String> infoUpdateAction(HttpSession session, Member vo) {
	    Member loginUser = (Member) session.getAttribute("loginUser");
	    Map<String, String> response = new HashMap<>();
	    
	    if (loginUser == null) {
	        response.put("message", "로그인 후 이용 가능합니다.");
	        return response;
	    }

	    memberService.changeInfo(vo);
	    response.put("message", "정보 수정 완료");
	    return response;
	}
	
	// 닉네임 중복 확인 처리
	@GetMapping("/nick-check-form")
	@ResponseBody
	public Map<String, Object> nickCheckAction(@RequestParam("nickname") String nickname) {
	    int result = memberService.confirmNickname(nickname);
	    
	    Map<String, Object> response = new HashMap<>();
	    response.put("result", result);
	    
	    return response;
	}
	
	
	// 내가 작성한 글 화면
	@GetMapping("/myWrite")
	public String myWriteView(HttpSession session, Model model) {
		Member loginUser = (Member)session.getAttribute("loginUser");
		
		if(loginUser == null) {
			model.addAttribute("message", "로그인 후 이용 가능합니다.");
			model.addAttribute("messageType", "info");
			return "redirect:/login";
		}
//		List<> writeList = Service.getMyWrite(loginUser.getId());
//		model.addAttribute("writeList", writeList);
		
		return "mypage/myWrite";
	}
	
	// 즐겨찾기한 글
	@GetMapping("/myCheck")
	public String myCheckView(HttpSession session, Model model) {
		Member loginUser = (Member)session.getAttribute("loginUser");
		
		if(loginUser == null) {
			model.addAttribute("message", "로그인 후 이용 가능합니다.");
			model.addAttribute("messageType", "info");
			return "redirect:/login";
		}
//		List<> checkList = Service.getMyCheck(loginUser.getId());
//		model.addAttribute("checkList", checkList);
		
		return "mypage/myCheck";
	}
}
