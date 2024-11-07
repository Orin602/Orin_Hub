package com.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.demo.domain.Member;
import com.demo.domain.Notice;
import com.demo.domain.Qna;
import com.demo.service.NoticeService;
import com.demo.service.QnaService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CustomerController {
	
	@Autowired
	private NoticeService noticeService;
	@Autowired
	private QnaService qnaService;

	@GetMapping("/customer")
	public String customerMainView() {
		
		
		return "customer/customerMain";
	}
	
	@GetMapping("/notice")
	public String noticeView(HttpSession session, Model model) {
		Member loginUser = (Member)session.getAttribute("loginUser");
		
		List<Notice> notices = noticeService.getAllNotices();
		model.addAttribute("notices", notices);
		
		return "customer/notice";
	}
	// 공지사항 상세
	@GetMapping("/customer_notice_detail")
	public String noticeDetailView(HttpSession session, Model model,
			@RequestParam("notice_seq") int notice_seq) {
		
		Notice notice = noticeService.getNoticeById(notice_seq);
		
		List<String> uploadImages = notice.getUploadedImages();
		model.addAttribute("uploadImages", uploadImages);
		model.addAttribute("notice", notice);
		
		return "customer/notice_detail";
	}
	@GetMapping("/fav_qna")
	public String qnaView(HttpSession session, Model model) {
		Member loginUser = (Member)session.getAttribute("loginUser");
		
		List<Qna> fixQna = qnaService.getFixQna();
		model.addAttribute("fixQna", fixQna);
		
		return "customer/qna";
	}
	
	//qna write
	@GetMapping("/qna_write")
	public String qnaWriteView(HttpSession session, Model model) {	//질문 작성 페이지
		Member loginUser = (Member)session.getAttribute("loginUser");
		
		if(loginUser == null) {
			model.addAttribute("message", "로그인 페이지로 이동");
			model.addAttribute("text", "질문 작성은 로그인 후 이용 가능합니다.");
			model.addAttribute("messageType", "info");
			
			return "login/login";
		}
		
		return "customer/qna_write";
	}
	@PostMapping("/qna-write-action")
	public String qnaWriteAction(HttpSession session, Model model, Qna qna) {
		Member loginUser = (Member) session.getAttribute("loginUser");
		
		if(loginUser == null) {
			model.addAttribute("message", "로그인 페이지로 이동");
			model.addAttribute("text", "질문 작성은 로그인 후 이용 가능합니다.");
			model.addAttribute("messageType", "info");
			
			return "login/login";
		}
		
		qna.setMember(loginUser);
		qnaService.createQna(qna);
		
		return "customer/qna";
	}
	
	//qna update
	@GetMapping("/qnaEdit")
	public String qnaEditView(@RequestParam("qna_seq") int qna_seq, Model model,
								HttpSession session) {
		Member loginUser = (Member)session.getAttribute("loginUser");
		
		Qna qna = qnaService.findQnaBySeq(qna_seq);

		if(loginUser == null) {
			model.addAttribute("message", "로그인 페이지로 이동");
			model.addAttribute("text", "질문 수정은 로그인 후 이용 가능합니다.");
			model.addAttribute("messageType", "info");
			
			return "login/login";
		}
		model.addAttribute("qna", qna);
		
		return "customer/qna_edit";
	}
	@PostMapping("/qna-edit-action")
	public String qnaUpdateAction(HttpSession session, Model model, Qna qna,
			@RequestParam("qna_seq") int qna_seq) {
		Member loginUser = (Member) session.getAttribute("loginUser");
		
		Qna updateQna = qnaService.findQnaBySeq(qna_seq);
		
		if(loginUser == null) {
			model.addAttribute("message", "로그인 페이지로 이동");
			model.addAttribute("text", "질문 수정은 로그인 후 이용 가능합니다.");
			model.addAttribute("messageType", "info");
			
			return "login/login";
		}
		updateQna.setTitle(qna.getTitle());
		updateQna.setContent(qna.getContent());
		qnaService.updateQna(updateQna);
		
		return "redirect:/myqna";
	}
	
	//qna delete
	@GetMapping("/deleteQna")
	public String deleteQna(@RequestParam("qna_seq") int qna_seq, Model model, HttpSession session) {
        Member loginUser = (Member) session.getAttribute("loginUser");
        
        if (loginUser == null) {
            model.addAttribute("message", "로그인 페이지로 이동");
            model.addAttribute("text", "질문 삭제는 로그인 후 이용 가능합니다.");
            model.addAttribute("messageType", "info");
            return "login/login";
        }

        try {
            // QnA 삭제 메소드 호출
            qnaService.deleteQna(qna_seq); // qna_seq 전달
            model.addAttribute("message", "QnA가 성공적으로 삭제되었습니다.");
            model.addAttribute("messageType", "success");
        } catch (IllegalArgumentException e) {
            // 삭제 실패 시 메시지 처리
            model.addAttribute("message", e.getMessage());
            model.addAttribute("messageType", "error");
        }
        
        // 삭제 후 목록 페이지로 리다이렉트
        return "redirect:/myqna";
    }
	
	
	
}
