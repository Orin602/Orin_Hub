package com.demo.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.demo.domain.CommunityBoard;
import com.demo.domain.MemberData;
import com.demo.service.CommunityBoardService;

import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@Controller
public class CommunityController {
	
	@Autowired
	CommunityBoardService communityse;
	
	@Autowired
	EntityManager entityManager;
    
	// 게시글 목록 조회
	@GetMapping("/community_list")
	public String getboard_list(@RequestParam(value = "community_seq", defaultValue = "1") int community_seq,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "size", defaultValue = "8") int size, Model model) {
		Page<CommunityBoard> pageList = communityse.getAllCommunityBoard(community_seq, page, size);
		List<CommunityBoard> boardList = pageList.getContent();

		model.addAttribute("boardList", boardList);
		model.addAttribute("pageInfo", pageList);
		return "comboard/CommunityList";
	}

	// 게시글 상세정보 조회
	@GetMapping("/community_detail")
	public String getCommunityBoardView(@RequestParam("community_seq") int community_seq, Model model, HttpSession session) {
		CommunityBoard comboard = communityse.getCommunityBoard(community_seq);
		Set<Integer> viewed = (Set<Integer>) session.getAttribute("viewed");
		
		
		 if (viewed == null) { // 처음클릭시
		        viewed = new HashSet<>();
		        session.setAttribute("viewed", viewed);
		    }

		    if (!viewed.contains(community_seq)) {
		        if (comboard != null) {
		            comboard.setCommunity_cnt(comboard.getCommunity_cnt() + 1);  // 조회수 1 증가
		            communityse.updateBoard(comboard);  // DB에 업데이트
		        }
		        viewed.add(community_seq); //방문한 게시글번호 추가
		    }
	    
		model.addAttribute("CommunityBoard", comboard);
		model.addAttribute("community_seq", comboard.getCommunity_seq());

		return "comboard/CommunityDetail";
	}

	// 제목, 작성자로 검색
	@GetMapping("/communityboard_search")
	public String getSearchByType(@RequestParam(value = "community_seq", defaultValue = "1") int community_seq,
			@RequestParam("searchType") String searchType, @RequestParam("searchKeyword") String keyword,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "size", defaultValue = "8") int size, Model model) {

		Page<CommunityBoard> pageList;

		if ("title".equals(searchType)) {
			pageList = communityse.getCommunityBoardByTitle(community_seq, page, size, keyword);
			List<CommunityBoard> searchResult = pageList.getContent();
			model.addAttribute("boardList", searchResult);
			model.addAttribute("pageInfo", pageList);

		} else {
			pageList = communityse.getCommunityBoardById(community_seq, page, size, keyword);
			List<CommunityBoard> searchResult = pageList.getContent();
			model.addAttribute("boardList", searchResult);
			model.addAttribute("pageInfo", pageList);
		}

		return "comboard/CommunityList";
	}

	// 글쓰기 페이지로 이동
	@GetMapping("/communityboard_write")
	public String getCommunityBoardWriteView(HttpSession session, Model model) {
		MemberData loginUser =  (MemberData)session.getAttribute("loginUser");

			if (loginUser == null) { 
				return "member/login"; 
			} else {
		return "comboard/CommunityWrite";
			}
	}
    
	// 글쓰기 등록
	@Transactional
	@PostMapping("/communityboard_write_t")
	public String insertCommunity(HttpSession session, Model model,
			@RequestParam("title") String title,
	        @RequestParam("content") String content) {

		MemberData loginUser =  (MemberData)session.getAttribute("loginUser");
		Page<CommunityBoard> pageInfo = (Page<CommunityBoard>)session.getAttribute("pageInfo");
		
		if (loginUser != null) {
        	loginUser = entityManager.merge(loginUser); 
            entityManager.persist(loginUser);
        }
		
		if (pageInfo == null) {
	        pageInfo = communityse.getAllCommunityBoard(1, 1, 8); // 기본값 설정
	    }
		
		List<CommunityBoard> boardList = pageInfo.getContent();
        
	    
        CommunityBoard community = new CommunityBoard();  
	    
        community.setCommunity_content(content);
        community.setCommunity_title(title);
        community.setMember_data(loginUser);

		communityse.insertBoard(community);
		
		model.addAttribute("boardList", boardList);
		model.addAttribute("pageInfo", pageInfo );
		
		return "redirect:/community_list";

	}
	

		
		// 글수정 페이지로 이동
		@GetMapping("/communityboard_update")
		public String getCommunityBoardUpdateView(HttpSession session, Model model, @RequestParam("community_seq") int community_seq) {
			MemberData loginUser =  (MemberData)session.getAttribute("loginUser");
			CommunityBoard board = communityse.getCommunityBoard(community_seq);

				if (loginUser == null) { 
					return "member/login"; 
				} else {
					model.addAttribute("communityboardVO", board);
					model.addAttribute("communityboardVO.community_seq", board.getCommunity_seq());
			        }
					
					return "comboard/Communityupdate";
				
		}
		
	// 글 수정
	@PostMapping("/communityboard_update_t")
	public String updateCom_Board(HttpSession session, @RequestParam("community_seq") int community_seq,
			Model model,
			@RequestParam("title") String title,
	        @RequestParam("content") String content) {
		
		MemberData loginUser =  (MemberData)session.getAttribute("loginUser");
		CommunityBoard board = communityse.getCommunityBoard(community_seq);
        
		if (loginUser == null) { 
			return "member/login"; 
		}else if(!(loginUser.getId()).equals(board.getMember_data().getId())){
			return "본인이 작성한 글만 수정가능합니다.";
		}else {

	        CommunityBoard community = new CommunityBoard();  
		    
	        community.setCommunity_content(content);
	        community.setCommunity_title(title);
	        community.setMember_data(loginUser);
	        community.setCommunity_seq(community_seq);
		    
		    
		communityse.updateBoard(community);
		return "redirect:/community_detail?community_seq=" + community_seq;
	}
	}

	// 글 삭제
	@GetMapping("/communityboard_delete")
	public String deleteCom_Board(@RequestParam(value = "community_seq") int community_seq, HttpSession session) {
		MemberData loginUser = (MemberData) session.getAttribute("loginUser");
		CommunityBoard board = communityse.getCommunityBoard(community_seq);

		if (loginUser == null) { 
			return "member/login"; 
		}else if(!(loginUser.getId()).equals(board.getMember_data().getId())){
			return "본인이 작성한 글만 삭제가능합니다.";
		}else {	
		communityse.deleteBoard(board);
		return "redirect:/community_list";
	}
	}
	
	
	@PostMapping("/community_goodpoint")
	public String goodPoint_Action(@RequestParam("community_seq") int community_seq, HttpSession session) {
		CommunityBoard com_board = communityse.getCommunityBoard(community_seq);
	    HashMap<Integer, String> goodPointcommunityStatusMap = (HashMap<Integer, String>) session.getAttribute("goodPointcommunityStatusMap");

	    if (goodPointcommunityStatusMap == null) {
	        goodPointcommunityStatusMap = new HashMap<>();
	        session.setAttribute("goodPointcommunityStatusMap", goodPointcommunityStatusMap);
	    }

	    String goodPointStatus = goodPointcommunityStatusMap.get(community_seq);

	    if (goodPointStatus == null || goodPointStatus.equals("off")) {
	        if (com_board != null) {
	            com_board.setCommunity_goodpoint(com_board.getCommunity_goodpoint() + 1);
	            communityse.updateBoard(com_board);
	            goodPointcommunityStatusMap.put(community_seq, "on");
	        }
	    } else if (goodPointStatus.equals("on")) {
	        if (com_board != null) {
	        	com_board.setCommunity_goodpoint(com_board.getCommunity_goodpoint() - 1);
	            communityse.updateBoard(com_board);
	            goodPointcommunityStatusMap.put(community_seq, "off");
	        }
		    }
		    
		return "redirect:/community_detail?community_seq=" + community_seq;
	}
}

