package com.demo.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.demo.domain.FileUploadUtil;
import com.demo.domain.Member;
import com.demo.domain.Notice;
import com.demo.service.MemberService;
import com.demo.service.NoticeService;

import jakarta.servlet.http.HttpSession;

@Controller
@SessionAttributes("admin")
public class AdminContoller {

	@Autowired
	private MemberService memberService;
	@Autowired
	private NoticeService noticeService;
	
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
	public String updateNotice(HttpSession session, Model model, Notice notice,
			@RequestParam("uploadFile") MultipartFile[] uploadFile,
			@RequestParam("notice_seq") int notice_seq) {
		Member admin = (Member)session.getAttribute("admin");
		Notice vo = noticeService.getNoticeById(notice_seq);
		
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
		vo.setTitle(notice.getTitle());
		vo.setContent(notice.getContent());
		// 기존 이미지 리스트 유지
		List<String> existingImages = vo.getUploadedImages();
		if(existingImages == null) {
			existingImages = new ArrayList<>();
		}
		// 새로 업로드된 이미지 추가
		if(notice.getUploadedImages() != null) {
			existingImages.addAll(notice.getUploadedImages());
		}
		
		// 수정된 이미지 리스트 설정
		vo.setUploadedImages(existingImages);
		// 파일 업로드
		if(uploadFile.length > 0 || !vo.getUploadedImages().isEmpty()) {
			List<String> fileUrls = new ArrayList<>();
			
			if(!vo.getUploadedImages().isEmpty()) {
				fileUrls.addAll(vo.getUploadedImages());
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
			vo.setUploadedImages(fileUrls);
		}
		noticeService.updateNotice(vo);
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
	@GetMapping("/admin-customer-list")
	public String allCustomerList(HttpSession session, Model model) {
	    // 회원 목록을 가져오는 로직 구현
	    return "admin/section/customer_list"; // 뷰 이름
	}

	@GetMapping("/admin-fix-qna")
	public String fixedQuestions(HttpSession session, Model model) {
	    // 고정 질문 목록을 가져오는 로직 구현
	    return "admin/section/fixed_questions"; // 뷰 이름
	}

	@GetMapping("/admin-customer-qna")
	public String customerQuestions(HttpSession session, Model model) {
	    // 회원 질문 목록을 가져오는 로직 구현
	    return "admin/section/customer_questions"; // 뷰 이름
	}

	@GetMapping("/admin-review")
	public String reviewList(HttpSession session, Model model) {
	    // 리뷰 목록을 가져오는 로직 구현
	    return "admin/section/review_list"; // 뷰 이름
	}

	@GetMapping("/admin-apply")
	public String commentList(HttpSession session, Model model) {
	    // 댓글 목록을 가져오는 로직 구현
	    return "admin/section/comment_list"; // 뷰 이름
	}

}
