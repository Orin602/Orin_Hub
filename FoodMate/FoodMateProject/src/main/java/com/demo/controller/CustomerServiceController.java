package com.demo.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.demo.domain.AdminQnaBoard;
import com.demo.domain.MemberData;
import com.demo.domain.askBoard;
import com.demo.service.CustomerService;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@Controller
@RequestMapping("/")
@SessionAttributes("loginUser")
public class CustomerServiceController {

    private final CustomerService customerService;

    @Autowired
    public CustomerServiceController(CustomerService customerService) {
        this.customerService = customerService;
    }

    
    @GetMapping("/inquiry/inquiryForm")
    public String showInquiryForm(Model model, HttpSession session) {
        MemberData loginUser = (MemberData) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }
        return "inquiry/inquiryForm";
    }

    
 // 1:1 문의 저장
    @PostMapping("/saveInquiry")
    public String saveInquiry(@ModelAttribute("loginUser") MemberData loginUser, askBoard vo, @RequestParam("agree") boolean agree) {
    	if (!agree) {
            // 개인정보 동의하지 않은 경우
            return "redirect:/inquiry/inquiryList?error=agreeRequired";
        }
    	
    	// 현재 사용자의 이름 가져오기
        String username = loginUser.getName();
        
        // 사용자 정보를 이용하여 작업 수행
        askBoard inquiry = new askBoard();
        inquiry.setMember_data(loginUser); // 로그인한 사용자 정보 설정
        inquiry.setName(username); // 사용자 이름 설정
        inquiry.setEmail(vo.getEmail()); // 이메일 설정
        inquiry.setSubject(vo.getSubject()); // 제목 설정
        inquiry.setMessage(vo.getMessage()); // 메시지 설정
        inquiry.setRegdate(new Date()); // 현재 시간으로 설정
        
        // 1:1 문의 저장
        customerService.addInquiry(inquiry);
        
        return "redirect:/inquiry/inquiryList";
    }





    // 1:1 문의 목록 보기
    @GetMapping("/inquiry/inquiryList")
    public String showInquiryList(Model model, HttpSession session) {
        // 세션에서 로그인 사용자 정보 가져오기
        MemberData loginUser = (MemberData) session.getAttribute("loginUser");
        
        // 만약 로그인 되어 있지 않다면 로그인 페이지로 리다이렉트
        if (loginUser == null) {
            return "redirect:/login";
        }

        // inquiryList를 가져와서 모델에 추가
        List<askBoard> inquiries = customerService.getInquiryList();

        // 현재 로그인된 사용자의 이름 가져오기
        String username = loginUser.getName();

        // 모델에 현재 사용자의 이름 추가
        model.addAttribute("username", username);
        model.addAttribute("inquiries", inquiries);
        return "inquiry/inquiryList";
    }


    @GetMapping("/inquiry/inquiry_detail/{id}")
    public String showInquiryDetails(@PathVariable Long id, Model model) {
        askBoard inquiryDetail = customerService.getInquiryDetailsById(id);
        if (inquiryDetail == null) {
            return "redirect:/error";
        }
        model.addAttribute("inquiryDetail", inquiryDetail);
        return "inquiry/inquiry_detail";
    }

    @GetMapping("qna/all")
    public String showQnaList(Model model) {
        List<AdminQnaBoard> qnaList = customerService.getAllQnaBoards();
        model.addAttribute("qnaList", qnaList);
        return "qna/all";
    }

    @GetMapping("/qna/qna_detail/{id}")
    public String showQnaDetails(@PathVariable Long id, Model model) {
        AdminQnaBoard qnaDetail = customerService.getQnaDetailsById(id);
        if (qnaDetail == null) {
            return "redirect:/error";
        }
        model.addAttribute("qnaDetail", qnaDetail);
        return "qna/qna_detail";
    }

    @GetMapping("/home")
    public String homePage(Model model) {
        model.addAttribute("welcomeMessage", "건강한 식단을 추천해드릴게요!");
        return "main";
    }

    @GetMapping("/customer_service")
    public String showCustomerServicePage(Model model) {
        return "customer_service/customer_service";
    }

    @GetMapping("/customer_service/introduce")
    public String showIntroduce(Model model) {
        return "/customer_service/introduce";
    }

}
