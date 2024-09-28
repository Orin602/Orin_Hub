package com.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomerController {

	@GetMapping("/customer")
	public String customerMainView() {
		
		
		return "customer/customerMain";
	}
	
	@GetMapping("/notice")
	public String noticeView() {
		
		
		return "customer/notice";
	}
	
	@GetMapping("/fav_qna")
	public String qnaView() {
		
		
		return "customer/qna";
	}
}
