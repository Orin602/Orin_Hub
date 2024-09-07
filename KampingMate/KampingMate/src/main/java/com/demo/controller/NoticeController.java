package com.demo.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.demo.domain.Notice;
import com.demo.service.NoticeService;

import jakarta.servlet.http.HttpSession;

@Controller
public class NoticeController {
	
	@Autowired
    NoticeService noticesv;
	
	 //공지 리스트
    @GetMapping(value = {"/notice", "/Notice_list"})
    public String getNoticeList(@RequestParam(value = "notice_seq", defaultValue = "1") int notice_seq,
                                @RequestParam(value = "page", defaultValue = "1") int page,
                                @RequestParam(value = "size", defaultValue = "6") int size, Model model) {
        Page<Notice> pageList = noticesv.getAllNotice(notice_seq, page, size);
        List<Notice> NoticeList = pageList.getContent();
        
        long totalElements = pageList.getTotalElements();
        long startNumber = totalElements - (page - 1) * size;

        model.addAttribute("NoticeList", NoticeList);
        model.addAttribute("totalPages", pageList.getTotalPages());
        model.addAttribute("pageNumber", page);
        model.addAttribute("startNumber", startNumber);

        return "Community/NoticeList";
    }
    
 // 게시글 상세정보 조회
 	@GetMapping("/Notice_detail")
 	public String getNoticeView(@RequestParam("notice_seq") int notice_seq, Model model, HttpSession session) {
 		Notice vo = noticesv.getNotice(notice_seq);
 		Set<Integer> viewed = (Set<Integer>) session.getAttribute("viewed");
 		
 		
 		 if (viewed == null) { // 처음클릭시
 		        viewed = new HashSet<>();
 		        session.setAttribute("viewed", viewed);
 		    }

 		    if (!viewed.contains(notice_seq)) {
 		        if (vo != null) {
 		            vo.setNotice_cnt(vo.getNotice_cnt() + 1);  // 조회수 1 증가
 		            noticesv.updateNotice(vo);  // DB에 업데이트
 		        }
 		        viewed.add(notice_seq); //방문한 게시글번호 추가
 		    }
 	    
 		model.addAttribute("noticeVO", vo);

 		return "Community/NoticeDetail";
 	}
 	
 // 제목으로 검색
 	@GetMapping("/Notice_search")
 	public String getSearchByType(
 			@RequestParam("searchKeyword") String keyword,
 			@RequestParam(value = "page", defaultValue = "1") int page,
 			@RequestParam(value = "size", defaultValue = "6") int size, Model model) {
 		
 		Page<Notice> pageList = noticesv.getNoticeByNotice_Title( page, size, keyword);	    

 	    List<Notice> searchResult = pageList.getContent();
 	    long totalElements = pageList.getTotalElements();
 	    long startNumber = totalElements - (page - 1) * size;

 	    model.addAttribute("NoticeList", searchResult);
 	    model.addAttribute("totalPages", pageList.getTotalPages());
 	    model.addAttribute("pageNumber", page);
 	    model.addAttribute("startNumber", startNumber);

 		return "Community/NoticeList";
 	}
 	
 	//카테고리별 분류
 		@GetMapping("/category")
 		public String com_BoardKindAction(
 				@RequestParam(value = "page", defaultValue = "1") int page,
 				@RequestParam(value = "size", defaultValue = "6") int size,
 				Model model, @RequestParam("category") String category) {
 			Page<Notice> pageList = noticesv.getNoticeBynotice_cate(page, size, category);
 			List<Notice> kindlist = pageList.getContent();
 			
 	 	    long totalElements = pageList.getTotalElements();
 	 	    long startNumber = totalElements - (page - 1) * size;
 			
 			model.addAttribute("NoticeList", kindlist);
 			model.addAttribute("totalPages", pageList.getTotalPages());
 	 	    model.addAttribute("pageNumber", page);
 	 	    model.addAttribute("startNumber", startNumber);
 			
 			return "Community/NoticeList";
 		}

}
