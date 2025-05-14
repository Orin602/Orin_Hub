package com.demo.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.demo.domain.FileUploadUtil;
import com.demo.domain.Member;
import com.demo.domain.Notice;
import com.demo.domain.OrderStatus;
import com.demo.domain.Qna;
import com.demo.domain.Store;
import com.demo.service.MemberService;
import com.demo.service.NoticeService;
import com.demo.service.QnaService;
import com.demo.service.ReviewService;
import com.demo.service.StoreService;

import jakarta.servlet.http.HttpSession;

@Controller
@SessionAttributes("admin")
public class AdminContoller {

	@Autowired
	private MemberService memberService;
	@Autowired
	private NoticeService noticeService;
	@Autowired
	private QnaService qnaService;
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private StoreService storeService;
	
	@GetMapping("/admin")
	public String adminLogin() {
		return "admin/admin_login";
	}
	
	// 관리자 로그인 처리
	@PostMapping("/admin_ok")
	public String adminMainView(HttpSession session, Member vo, Model model) {
		int result = memberService.adminId(vo);
		model.addAttribute("message", null); // 초기화
		
		if(result == 1) {
			Member admin = memberService.getMember(vo.getId());
			session.setAttribute("admin", admin);	// 세션에 로그인 사용자 정보 저장
			model.addAttribute("admin", admin);
			return "redirect:/adminMain"; // 리다이렉션을 사용하여 URL 변경
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
	@GetMapping("/adminMain")
	public String adminMainPage(HttpSession session, Model model) {
		Member admin = (Member)session.getAttribute("admin");
		if(admin == null) {
			model.addAttribute("message", "로그인 페이지로 이동..");
			model.addAttribute("messageType", "info");
			
			return "admin/admin_login";
		}
		model.addAttribute("admin", admin);
		return "admin/adminMain";
	}
	
	@GetMapping("/update_graph")
    public String updateGraph() {
        return "admin/section/update_graph";
    }
	
	// 관리자 로그아웃
	@GetMapping("/admin-logout")
	public String adminLogout(HttpSession session) {
		session.invalidate(); // 현재 세션을 무효화
		return "main";
	}
	
	// 모든 공지사항
	@GetMapping("/admin-notice-list")
	public String allNoticeList(HttpSession session, Model model) {
		Member admin = (Member)session.getAttribute("admin");

		if(admin == null) {
			model.addAttribute("message", "로그인 페이지로 이동..");
			model.addAttribute("text", "공지사항 작성은 관리자 로그인 후 이용 가능합니다.");
			model.addAttribute("messageType", "info");
			
			return "admin/admin_login";
		}
		model.addAttribute("admin", admin);
	    List<Notice> notices = noticeService.getAllNotices();	
	    
	    model.addAttribute("notices", notices);
	    return "admin/section/notice_list"; // 뷰 이름
	}

	// 관리자 공지사항 작성
	@GetMapping("/admin-notice-write")
	public String writeNotice(HttpSession session, Model model) {
		Member admin = (Member)session.getAttribute("admin");
		if(admin == null) {
			model.addAttribute("message", "로그인 페이지로 이동");
            model.addAttribute("text", "회원관리를 위해 로그인해주세요.");
            model.addAttribute("messageType", "info");
            
            return "admin/admin_login";
		}
	    
	    return "admin/section/notice_write"; // 뷰 이름
	}

	// 관리자 공지사항 작성 처리
	@PostMapping("/notice-write-action")
	public String writeNoticeAction(HttpSession session, Model model, Notice notice,
			@RequestParam("uploadFile") MultipartFile[] uploadFile) {
		Member admin = (Member)session.getAttribute("admin");
		
		if(admin == null) {
			model.addAttribute("message", "로그인 페이지로 이동..");
			model.addAttribute("text", "공지사항 작성은 관리자 로그인 후 이용 가능합니다.");
			model.addAttribute("messageType", "info");
			
			return "admin/admin_login";
		}
		notice.setMember(admin);
		
		List<String> fileUrls = new ArrayList<>();
		for (MultipartFile file : uploadFile) {
			if(!file.isEmpty()) {
				// 경로
				String uploadDir = "C:/ThisIsJava/SpringBootWorkspace/Book/uploads2/";
				// 파일이름 수정
				String originalName = file.getOriginalFilename();
				String fileExtension = originalName.substring(originalName.lastIndexOf("."));
				String uuid = UUID.randomUUID().toString();
				String fileName = uuid + fileExtension;
				
				try {
					// 파일 저자ㅇ
					FileUploadUtil.saveFile(uploadDir, fileName, file);
					// url 생성
					String fileUrl = "/uploads2/" + fileName;
					fileUrls.add(fileUrl);
				} catch (IOException e) {
					e.printStackTrace();
					model.addAttribute("message", "파일 업로드 중 오류 발생");
					model.addAttribute("messageType", "error");
				}
				notice.setUploadedImages(fileUrls);
			}
		}
		notice.setContent(notice.getContent().replace("\r\n", "<br>"));
		noticeService.createNotice(notice);
		
		return "redirect:/admin-notice-list";
	}
	
	// 공지사항 상세
	@GetMapping("/notice_detail")
	public String noticeDetailView(HttpSession session, Model model,
			@RequestParam("notice_seq") int notice_seq) {
		Member admin = (Member)session.getAttribute("admin");
		if(admin == null) {
			model.addAttribute("message", "로그인 페이지로 이동");
            model.addAttribute("text", "공지사항 수정을 위해 로그인해주세요.");
            model.addAttribute("messageType", "info");
            
            return "admin/admin_login";
		}
		Notice notice = noticeService.getNoticeById(notice_seq);
		
		List<String> uploadImages = notice.getUploadedImages();
		model.addAttribute("uploadImages", uploadImages);
		model.addAttribute("notice", notice);
		model.addAttribute("admin", admin);
		
		return "admin/section/notice_detail";
	}
	
	// 공지사항 수정
	@GetMapping("/admin-notice-ED")
	public String editDeleteNotice(HttpSession session, Model model,
			@RequestParam("notice_seq") int notice_seq) {
		Member admin = (Member)session.getAttribute("admin");
		Notice notice = noticeService.getNoticeById(notice_seq);
		
		if(admin == null) {
			model.addAttribute("message", "로그인 페이지로 이동");
            model.addAttribute("text", "공지사항 수정을 위해 로그인해주세요.");
            model.addAttribute("messageType", "info");
            
            return "admin/admin_login";
		}
		if(!notice.getMember().getId().equals(admin.getId())) { // 작성자와 로그인유저 비교
			model.addAttribute("message", "수정 불가");
			model.addAttribute("text", "작성자만 수정할 수 있습니다.");
			model.addAttribute("messageType", "error");
			
			return "redirect:/admin-notice-list";
		}
		model.addAttribute("notice", notice);
	    
	    return "admin/section/notice_edit";
	}
	// 공지사항 이미지 삭제
	@GetMapping("/delete-image-notice")
	@ResponseBody
	public ResponseEntity<String> deleteImageNotice(@RequestParam("notice_seq") int notice_seq,
			@RequestParam("imageIndex") int imageIndex) {
		try {
			noticeService.deleteImage(notice_seq, imageIndex);
			return ResponseEntity.ok("이미지 삭제 성공!");
		} catch(Exception e) {
			return ResponseEntity.status(500).body("이미지 삭제 실패 : " + e.getMessage());
		}
	}
	// 공지사항 수정 처리
	@PostMapping("/update-notice")
	public String updateNotice(HttpSession session, Model model, Notice vo,
			@RequestParam("uploadFile") MultipartFile[] uploadFile,
			@RequestParam("notice_seq") int notice_seq) {
		Member admin = (Member)session.getAttribute("admin");
		Notice notice = noticeService.getNoticeById(notice_seq);
		
		
		if(admin == null) {
			model.addAttribute("message", "로그인 페이지로 이동");
            model.addAttribute("text", "공지사항 수정을 위해 로그인해주세요.");
            model.addAttribute("messageType", "info");
            
            return "admin/admin_login";
		}
		if(!notice.getMember().getId().equals(admin.getId())) { // 작성자와 로그인유저 비교
			model.addAttribute("message", "수정 불가");
			model.addAttribute("text", "작성자만 수정할 수 있습니다.");
			model.addAttribute("messageType", "error");
			
			return "redirect:/admin-notice-list";
		}
		notice.setTitle(vo.getTitle());
		notice.setContent(vo.getContent().replace("\r\n", "<br>"));
		notice.setContent(vo.getContent());
		// 기존 이미지 리스트 유지
		List<String> existingImages = notice.getUploadedImages();
		if(existingImages == null) {
			existingImages = new ArrayList<>();
		}
		// 새로 업로드된 이미지 추가
		if(vo.getUploadedImages() != null) {
			existingImages.addAll(vo.getUploadedImages());
		}
		
		// 수정된 이미지 리스트 설정
		notice.setUploadedImages(existingImages);
		// 파일 업로드
		if(uploadFile.length > 0 || !notice.getUploadedImages().isEmpty()) {
			List<String> fileUrls = new ArrayList<>();
			
			if(!notice.getUploadedImages().isEmpty()) {
				fileUrls.addAll(notice.getUploadedImages());
			}
			for(MultipartFile file : uploadFile) {
				if(!file.isEmpty()) {
					// 경로
					String uploadDir = "C:/ThisIsJava/SpringBootWorkspace/Book/uploads2/";
					// 파일이름 수정
					String originalName = file.getOriginalFilename();
					String fileExtension = originalName.substring(originalName.lastIndexOf("."));
					String uuid = UUID.randomUUID().toString();
					String fileName = uuid + fileExtension;
					
					try {
						// 파일 저자ㅇ
						FileUploadUtil.saveFile(uploadDir, fileName, file);
						// url 생성
						String fileUrl = "/uploads2/" + fileName;
						fileUrls.add(fileUrl);
					} catch (IOException e) {
						e.printStackTrace();
						model.addAttribute("message", "파일 업로드 중 오류 발생");
						model.addAttribute("messageType", "error");
						
						return "redirect:/admin-notice-list";
					}
				}
			}
			notice.setUploadedImages(fileUrls);
		}
		noticeService.updateNotice(notice);
		return "redirect:/admin-notice-list";
	}

	// 공지사항 삭제 처리
	@DeleteMapping("/notice/delete")
	public ResponseEntity<String> deleteNotice(@RequestParam("notice_seq") int notice_seq) {
		try {
			noticeService.deleteNotice(notice_seq);
			return ResponseEntity.ok("공지사항 삭제 완료");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("삭제 실패: " + e.getMessage());
		}
	}
	
	// 회원관리 페이지
	@GetMapping("/admin-customer-list")
	public String allCustomerList(HttpSession session, Model model) {
		Member admin = (Member)session.getAttribute("admin");
		if(admin == null) {
			model.addAttribute("message", "로그인 페이지로 이동");
            model.addAttribute("text", "회원관리를 위해 로그인해주세요.");
            model.addAttribute("messageType", "info");
            
            return "admin/admin_login";
		}
		// 회원 목록을 가져오는 로직 구현
		List<Member> members = memberService.getAllMembers();
		model.addAttribute("members", members);
	    return "admin/section/customer_list"; // 뷰 이름
	}
	// 회원 구매 목록
	@GetMapping("/admin-customer-orderlist")
	public String customerOrderList(HttpSession session, Model model) {
		Member admin = (Member)session.getAttribute("admin");
		if(admin == null) {
			model.addAttribute("message", "로그인 페이지로 이동");
            model.addAttribute("text", "회원관리를 위해 로그인해주세요.");
            model.addAttribute("messageType", "info");
            
            return "admin/admin_login";
		}
		List<Store> stores = storeService.getAllOrder();
		model.addAttribute("stores", stores);
		return "admin/section/order_list";
	}
	// 회원코드 수정
	@PostMapping("/update-membercode")
    public ResponseEntity<String> updateMemberCode(HttpSession session, @RequestParam String id,
    		@RequestParam int newMemberCode, Model model) {
        Member admin = (Member) session.getAttribute("admin");
        
        if (admin == null) {
        	return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("로그인후 가능한 기능입니다."); // 401 Unauthorized 응답
        }
        
        memberService.updateMemberCode(id, newMemberCode); // 서비스 메서드 호출
        return ResponseEntity.ok("회원코드가 수정되었습니다."); // 성공 메시지 반환
    }
	// 회원 탈퇴 처리
	@PostMapping("/delete-member-admin")
	public ResponseEntity<String> deleteMemberAction (HttpSession session, Model model,
			@RequestParam("id") String memberId) {
		Member admin = (Member)session.getAttribute("admin");
		
		if(admin == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("로그인후 가능한 기능입니다."); // 401 Unauthorized 응답
		}
		
		try {
			Member member = memberService.getMember(memberId);
			
			if(member == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("회원 정보를 찾을 수 없습니다.");
			}
			memberService.deleteMember(member);
			
			return ResponseEntity.ok("회원 탈퇴가 성공적으로 처리되었습니다.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
					body("회원 탈퇴 처리 중 오류가 발생했습니다.");
		}
	}
	// 회원 주문 상태 처리
	@PostMapping("/update-status")
	@ResponseBody
	public ResponseEntity<String> updateStatus(HttpSession session, @RequestParam("storeseq") int storeseq,
			@RequestParam("newStatus") OrderStatus newStatus) {
		System.out.println("POST 요청 처리됨: storeseq=" + storeseq + ", newStatus=" + newStatus);  // 디버깅용
	    Member admin = (Member)session.getAttribute("admin");

	    if (admin == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                .body("로그인이 필요합니다.");
	    }

	    try {
	        storeService.updateStatus(storeseq, newStatus);
	        return ResponseEntity.ok("주문 상태가 성공적으로 변경되었습니다.");
	    } catch (IllegalAccessException e) {
	        return ResponseEntity.badRequest().body(e.getMessage());
	    }
	}


	// 고정질문 페이지
	@GetMapping("/admin-fix-qna")
	public String fixedQuestions(HttpSession session, Model model) {
		Member admin = (Member)session.getAttribute("admin");
		if(admin == null) {
			model.addAttribute("message", "로그인 페이지로 이동");
            model.addAttribute("text", "질문관리를 위해 로그인해주세요.");
            model.addAttribute("messageType", "info");
            
            return "admin/admin_login";
		}
	    // 고정 질문 목록을 가져오는 로직 구현
		List<Qna> adminQna = qnaService.getFixQna();
		model.addAttribute("adminQna", adminQna);
		
	    return "admin/section/fixed_questions"; // 뷰 이름
	}
	// 고정질문 작성 페이지
	@GetMapping("/fix-qna-write")
	public String fixQnaWriteView(HttpSession session, Model model) {
		Member admin = (Member)session.getAttribute("admin");
		
		if(admin == null) {
			model.addAttribute("message", "로그인 페이지로 이동");
            model.addAttribute("text", "고정질문 작성을 위해 로그인해주세요.");
            model.addAttribute("messageType", "info");
            
            return "admin/admin_login";
		}
		return "admin/section/fix_qna_write";
		
	}
	// 고정질문 작성 처리
	@PostMapping("/fix-qna-write-form")
	public String fixQnaWriteAction(HttpSession session, Model model, Qna fixQna) {
		Member admin = (Member)session.getAttribute("admin");
		
		if(admin == null) {
			model.addAttribute("message", "로그인 페이지로 이동");
            model.addAttribute("text", "고정질문 작성을 위해 로그인해주세요.");
            model.addAttribute("messageType", "info");
            
            return "admin/admin_login";
		}
		fixQna.setMember(admin);
		qnaService.createFixQna(fixQna);
		return "redirect:/admin-fix-qna";
	}
	// 고정질문 삭제 처리
	@PostMapping("/delete-fix-qna")
	public String deleteFixQnaAction(@RequestParam("qna_seq") int qna_seq, HttpSession session,
			Model model) {
		Member admin = (Member)session.getAttribute("admin");
		
		if(admin == null) {
			model.addAttribute("message", "로그인 페이지로 이동");
            model.addAttribute("text", "고정질문 삭제를 위해 로그인해주세요.");
            model.addAttribute("messageType", "info");
            
            return "admin/admin_login";
		}
		qnaService.deleteQna(qna_seq);
		return "redirect:/admin-fix-qna";		
	}
	// 고정질문 수정 페이지
	@GetMapping("/fix-qna-edit")
	public String editFixQnaView(@RequestParam("qna_seq")int qna_seq, Model model, HttpSession session) {
		Member admin = (Member)session.getAttribute("admin");
		
		if(admin == null) {
			model.addAttribute("message", "로그인 페이지로 이동");
            model.addAttribute("text", "질문 수정을 위해 로그인해주세요.");
            model.addAttribute("messageType", "info");
            
            return "admin/admin_login";
		}
		Qna qna = qnaService.findQnaBySeq(qna_seq);
		model.addAttribute("qna", qna);
		
		return "admin/section/fix_qna_edit";
	}
	// 고정질문 수정 처리
	@PostMapping("/fix-qna-edit-form")
	public String editFixQnaAction(HttpSession session, Model model, Qna vo) {
		Member admin = (Member)session.getAttribute("admin");
		
		if(admin == null) {
			model.addAttribute("message", "로그인 페이지로 이동");
            model.addAttribute("text", "질문 수정을 위해 로그인해주세요.");
            model.addAttribute("messageType", "info");
            
            return "admin/admin_login";
		}
		qnaService.updateQna(vo);
		
		return "redirect:/admin-fix-qna";
	}

	// 회원질문 페이지
	@GetMapping("/admin-customer-qna")
	public String customerQuestions(HttpSession session, Model model) {
		Member admin = (Member)session.getAttribute("admin");
		if(admin == null) {
			model.addAttribute("message", "로그인 페이지로 이동");
            model.addAttribute("text", "회원관리를 위해 로그인해주세요.");
            model.addAttribute("messageType", "info");
            
            return "admin/admin_login";
		}
	    // 회원 질문 목록을 가져오는 로직 구현
		List<Qna> customerQna = qnaService.getCustomerQna();
		model.addAttribute("customerQna", customerQna);
		
	    return "admin/section/customer_questions"; // 뷰 이름
	}
	// 회원질문 답변처리
	@PostMapping("/customer-qna-answer")
	@ResponseBody
	public ResponseEntity<String> submitCustomerQnaAnswer(HttpSession session, Model model,
			@RequestParam("qna_seq") int qna_seq, @RequestParam("answer") String answer) {
		Member admin = (Member)session.getAttribute("admin");
		if(admin == null) {
			model.addAttribute("message", "로그인 페이지로 이동");
            model.addAttribute("text", "회원관리를 위해 로그인해주세요.");
            model.addAttribute("messageType", "info");
            
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 필요");
		}
		
		Qna qna = qnaService.findQnaBySeq(qna_seq);
		if(qna != null) {
			qna.setAnswer(answer);
			qna.setAnswer_date(new Date());
			qna.setAnswer_status(1);
			
			qnaService.updateQna(qna);
			return ResponseEntity.ok("답변이 성공적으로 제출되었습니다.");
		} else {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("질문을 찾을 수 없습니다.");
		}
	}


}
