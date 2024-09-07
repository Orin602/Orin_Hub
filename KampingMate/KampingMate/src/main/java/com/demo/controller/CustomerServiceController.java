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

// 이 클래스가 Spring MVC 컨트롤러
@Controller
// 이 컨트롤러의 모든 요청은 루트 경로를 기반으로 처리
@RequestMapping("/") 
@SessionAttributes("loginUser") // 세션에 logiinUser 객체를 저장하도록 설정
public class CustomerServiceController {

	// 서비스 레이어의 의존성을 주입받아 비즈니스 로직을 처리
	@Autowired
    private final CustomerService customerService;

    // Spring이 자동으로 'CustomerService' 객체를 주입
    
    public CustomerServiceController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // GET 요청을 처리하여 1:1 문의 폼 보기
    @GetMapping("/inquiry/inquiryForm")
    public String showInquiryForm(Model model, HttpSession session) {
    	// 세션에서 로그인 사용자 정보 가져오기
        MemberData loginUser = (MemberData) session.getAttribute("loginUser");
        // 만약 로그인 되어 있지 않다면 로그인 페이지로 리다이렉트
        if (loginUser == null) {
            return "redirect:/login";
        }
        return "inquiry/inquiryForm";
    }

    
 // POST 요청을 처리하여 1:1 문의 저장
    @PostMapping("/saveInquiry")
    public String saveInquiry(@ModelAttribute("loginUser") MemberData loginUser, askBoard vo, @RequestParam("agree") boolean agree) {
    	// 사용자 동의 여부를 체크하고, 동의하지 않은 경우 리다이렉트하여 오류 메시지 표시
    	if (!agree) {
            // 개인정보 동의하지 않은 경우
            return "redirect:/inquiry/inquiryList?error=agreeRequired";
        }
    	
    	// 현재 사용자의 이름 가져오기
        String username = loginUser.getName();
        
        // 1:1 문의 객체 생성 밎 정보 설정
        // 사용자 정보를 이용하여 작업 수행
        // 'askBoard' 객체를 생성하고 사용자 정보와 문의 내용을 설정
        askBoard inquiry = new askBoard();
        inquiry.setMember_data(loginUser); // 로그인한 사용자 정보 설정
        inquiry.setName(username); // 사용자 이름 설정
        inquiry.setEmail(vo.getEmail()); // 이메일 설정
        inquiry.setSubject(vo.getSubject()); // 제목 설정
        inquiry.setMessage(vo.getMessage()); // 메시지 설정
        inquiry.setRegdate(new Date()); // 현재 시간으로 설정
        
        // 1:1 문의 데이터베이스에 저장
        customerService.addInquiry(inquiry);
        
        return "redirect:/inquiry/inquiryList"; //1:1 문의 목록 페이지로 리다이렉트
    }





    // GET 요청을 처리하여 1:1 문의 목록 보기
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

        // 모델에 현재 사용자의 이름과 1:1 문의 목록 추가
        model.addAttribute("username", username);
        model.addAttribute("inquiries", inquiries);
        return "inquiry/inquiryList";
    }

    // GET 요청을 처리하여 특정 ID의 1:1 문의 상세 정보 보기
    @GetMapping("/inquiry/inquiry_detail/{id}")
    // '@PathVariable Long id' : URL에서 문의 ID를 가져온다 
    public String showInquiryDetails(@PathVariable Long id, Model model) {
        askBoard inquiryDetail = customerService.getInquiryDetailsById(id);
        // 해당하는 1:1 문의가 없으면 에러 페이지로 리다이렉트
        if (inquiryDetail == null) {
            return "redirect:/error";
        }
        // 문의 상세 정보를 조회하고 모델에 추가
        model.addAttribute("inquiryDetail", inquiryDetail);
        return "inquiry/inquiry_detail"; // 1:1 문의 상세 정보 페이지 보여주기
    }

    // GET 요청을 처리하여 QnA 목록 보기
    @GetMapping("qna/all")
    public String showQnaList(Model model) {
    	// 모든 QnA 게시글을 조회하여 모델에 추가
        List<AdminQnaBoard> qnaList = customerService.getAllQnaBoards();
        model.addAttribute("qnaList", qnaList);
        return "qna/all"; // Q&A 목록 페이지 보여주기
    }

    // GET 요청을 처리하여 특정 ID의 QnA 상세 정보 보기
    @GetMapping("/qna/qna_detail/{id}")
    public String showQnaDetails(@PathVariable Long id, Model model) {
        AdminQnaBoard qnaDetail = customerService.getQnaDetailsById(id);
        // 해당하는 Q&A가 없으면 에러 페이지로 리다이렉트
        if (qnaDetail == null) {
            return "redirect:/error";
        }
        // QnA 상세 정보를 조회하고 모델에 추가
        model.addAttribute("qnaDetail", qnaDetail);
        return "qna/qna_detail"; // Q&A 상세 정보 페이지 보여주기
    }

    // GET 요청을 처리하여 홈 페이지 보기
    @GetMapping("/home")
    public String homePage(Model model) {
    	// 환영 메시지를 모델에 추가
        model.addAttribute("welcomeMessage", "건강한 식단을 추천해드릴게요!");
        return "main"; //메인 페이지 보여주기
    }

    // GET 요청을 처리하여 고객 서비스 페이지 보기
    @GetMapping("/customer_service")
    public String showCustomerServicePage(Model model) {
        return "customer_service/customer_service"; // 고객 서비스 페이지 보여주기
    }

}
