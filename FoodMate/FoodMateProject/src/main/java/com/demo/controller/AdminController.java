package com.demo.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.demo.domain.AdminQnaBoard;
import com.demo.domain.AdminRecipeBoard;
import com.demo.domain.askBoard;
import com.demo.domain.foodRecipe;
import com.demo.domain.MemberData;
import com.demo.service.AdminService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@SessionAttributes("loginUser")
public class AdminController {
	
	@Autowired
	AdminService adminService;
	
	/*
	 * 메인 페이지
	 */
	
	@GetMapping("/main.do")
    public String mainView(@ModelAttribute("loginUser") MemberData loginUser, Model model) {
        // loginUser가 존재하는지 확인하고 adminCheck 수행
        if (loginUser != null) {
            adminService.adminCheck(loginUser);
        } else {
        	return "admin/admin_main";    // 초기 로그인 화면으로 이동 **********************(수정필요)
        }
        
        List<AdminRecipeBoard> foodBoardList = adminService.getAllFoodBoardListMain();
        List<AdminQnaBoard> qnaBoardList= adminService.getAllQnaBoardListMain();
        List<askBoard> askBoardList = adminService.getAllAskBoardListMain();
        
        model.addAttribute("askBoardList", askBoardList);
        model.addAttribute("foodBoardList", foodBoardList);
        model.addAttribute("qnaBoardList", qnaBoardList);
        
        return "admin/admin_main";
    }
	
	/*
	 * 회원관리 페이지
	 */
	
	@GetMapping("/memberList.do")
	public String memberInfo(@ModelAttribute("loginUser") MemberData loginUser, Model model) {
		if (loginUser != null) {
            adminService.adminCheck(loginUser);
        } else {
        	return "admin/admin_main";    // 초기 로그인 화면으로 이동 **********************(수정필요)
        }
		
		List<MemberData> memberList = adminService.getAllMemberList();
		model.addAttribute("memberList", memberList);
		
		return "admin/boardPage/admin_userlist";  // 회원 정보 페이지로 이동
	}
	
	/*
	 * 관리자 음식게시판 페이지
	 */
	
	@GetMapping("/admin_food.do")
	public String foodBoard(@ModelAttribute("loginUser") MemberData loginUser, Model model, Pageable pageable) {
	    if (loginUser == null) {
	        return "redirect:/main.do";  // 초기 로그인 화면으로 리다이렉션
	    }

	    adminService.adminCheck(loginUser);

	    Page<AdminRecipeBoard> foodBoardPage = adminService.getAllFoodBoardList(PageRequest.of(pageable.getPageNumber(), 10));
	    model.addAttribute("foodBoardPage", foodBoardPage);

	    return "admin/boardPage/admin_foodboard";  // 관리자 음식레시피 게시판으로 이동
	}
	
	@GetMapping("/foodRegister.do")
	public String foodRegiView(@ModelAttribute("loginUser") MemberData loginUser) {
		if (loginUser != null) {
            adminService.adminCheck(loginUser);
        } 
		return "admin/boardForm/foodForm";
	}
	
	@PostMapping("/foodReg.do")  // 나머지 등록과정도 동일
	public String foodRegister(@ModelAttribute("loginUser") MemberData loginUser, AdminRecipeBoard vo) {
		// 로그인 정보를 Member타입으로 받아와서 vo객체에 비어있는 정보에 insert해야함
		// 로그인 영역에서 처리하는 방식대로 개선
		vo.setUserid(loginUser.getId());
		vo.setNickname(loginUser.getNickname());
		// 프로그램 실행해보고 vo객체 나머지가 제대로 들어오는지 확인***************
		adminService.adminCheck(loginUser);
		adminService.insertAdminRecipeBoard(vo);
		
		return "redirect:/admin_food.do";
	}
	
	@PostMapping("/foodEdit.do")
	public String editFood(@ModelAttribute("loginUser") MemberData loginUser, @RequestParam("foodSelect") int boardnum, RedirectAttributes redirectAttributes) {
		adminService.adminCheck(loginUser);
	    AdminRecipeBoard food = adminService.getByRecipeBoardnum(boardnum);

	    // 리다이렉트 어트리뷰트에 데이터 추가
	    redirectAttributes.addFlashAttribute("food", food);

	    // 데이터를 가지고 GET 방식으로 리다이렉트
	    return "redirect:/foodEditForm.do?boardnum=" + boardnum;
	}
	
	@GetMapping("/foodEditForm.do")
	public String showEditFoodForm(@RequestParam("boardnum") int boardnum, Model model) {
	    if (!model.containsAttribute("food")) {
	        // 리다이렉트가 아니라 직접 URL로 접근한 경우 필요한 데이터를 다시 로드
	        AdminRecipeBoard food = adminService.getByRecipeBoardnum(boardnum);
	        model.addAttribute("food", food);
	    }
	    return "admin/boardForm/foodEditForm";
	}
	
	@PostMapping("/foodUpdate.do")  // 나머지 등록과정도 동일
	public String foodUpdate(@ModelAttribute("loginUser") MemberData loginUser, @RequestParam("foodNum") int boardnum, 
							AdminRecipeBoard vo) {
		// 로그인 정보를 Member타입으로 받아와서 vo객체에 비어있는 정보에 insert해야함
		// 로그인 영역에서 처리하는 방식대로 개선
		vo.setRecipe_boardnum(boardnum);
		vo.setUserid(loginUser.getId());
		vo.setNickname(loginUser.getNickname());
		vo.setEditdate(new Date());
		// 프로그램 실행해보고 vo객체 나머지가 제대로 들어오는지 확인***************
		adminService.adminCheck(loginUser);
		adminService.updateAdminRecipeBoard(vo);
		
		return "redirect:/admin_food.do";
	}
	
	@PostMapping("/foodDelete.do")
    public String deleteFood(@ModelAttribute("loginUser") MemberData loginUser, @RequestParam("foodSelect") int boardnum) {
        // boardnum에 해당하는 Q&A 게시글을 삭제합니다.
		adminService.adminCheck(loginUser);
        adminService.deleteAdminRecipeBoard(boardnum);

        // 삭제 후 Q&A 목록 페이지로 리다이렉트합니다.
        return "redirect:/admin_food.do";
    }
	
	/*
	 *  관리자 Q&A 게시판 페이지
	 */
	
	@GetMapping("/admin_QnA.do")
	public String QnABoard(@ModelAttribute("loginUser") MemberData loginUser, Model model, Pageable pageable) {
		if (loginUser != null) {
            adminService.adminCheck(loginUser);
        } else {
        	return "redirect:/main.do";    // 초기 로그인 화면으로 이동 **********************(수정필요)
        }
		
		Page<AdminQnaBoard> qnaBoardPage = adminService.getAllQnaBoardList(PageRequest.of(pageable.getPageNumber(), 10));
	    model.addAttribute("qnaBoardPage", qnaBoardPage);

	    return "admin/boardPage/admin_qnaboard";  // 관리자 음식레시피 게시판으로 이동
	}
	
	@GetMapping("/qnaRegister.do")
	public String qnaRegiView(@ModelAttribute("loginUser") MemberData loginUser) {
		if (loginUser != null) {
            adminService.adminCheck(loginUser);
        } 
		return "admin/boardForm/qnaForm";
	}
	
	@PostMapping("/qnaReg.do")  // 나머지 등록과정도 동일
	public String qnaRegister(@ModelAttribute("loginUser") MemberData loginUser, AdminQnaBoard vo) {
		// 로그인 정보를 Member타입으로 받아와서 vo객체에 비어있는 정보에 insert해야함
		// 로그인 영역에서 처리하는 방식대로 개선
		// 프로그램 실행해보고 vo객체 나머지가 제대로 들어오는지 확인***************
		adminService.adminCheck(loginUser);
		adminService.insertAdminQnaBoard(vo);
		
		return "redirect:/admin_QnA.do";
	}
	
	@PostMapping("/qnaEdit.do")
	public String editQna(@ModelAttribute("loginUser") MemberData loginUser, @RequestParam("qnaSelect") int boardnum, RedirectAttributes redirectAttributes) {
		adminService.adminCheck(loginUser);
	    AdminQnaBoard qna = adminService.getByQnaBoardnum(boardnum);

	    // 리다이렉트 어트리뷰트에 데이터 추가
	    redirectAttributes.addFlashAttribute("qna", qna);

	    // 데이터를 가지고 GET 방식으로 리다이렉트
	    return "redirect:/qnaEditForm.do?boardnum=" + boardnum;
	}
	
	@GetMapping("/qnaEditForm.do")
	public String showEditQnaForm(@RequestParam("boardnum") int boardnum, Model model) {
	    if (!model.containsAttribute("qna")) {
	        // 리다이렉트가 아니라 직접 URL로 접근한 경우 필요한 데이터를 다시 로드
	        AdminQnaBoard qna = adminService.getByQnaBoardnum(boardnum);
	        model.addAttribute("qna", qna);
	    }
	    return "admin/boardForm/qnaEditForm";
	}
	
	@PostMapping("/qnaUpdate.do")  // 나머지 등록과정도 동일
	public String qnaUpdate(@ModelAttribute("loginUser") MemberData loginUser, @RequestParam("qnaNum") int boardnum, 
							AdminQnaBoard vo) {
		// 로그인 정보를 Member타입으로 받아와서 vo객체에 비어있는 정보에 insert해야함
		// 로그인 영역에서 처리하는 방식대로 개선
		vo.setQna_boardnum(boardnum);
		
		adminService.adminCheck(loginUser);
		adminService.updateAdminQnaBoard(vo);
		
		return "redirect:/admin_QnA.do";
	}
	
	@PostMapping("/qnaDelete.do")
    public String deleteQna(@ModelAttribute("loginUser") MemberData loginUser, @RequestParam("qnaSelect") int boardnum) {
        // boardnum에 해당하는 Q&A 게시글을 삭제합니다.
		adminService.adminCheck(loginUser);
        adminService.deleteAdminQnaBoard(boardnum);

        // 삭제 후 Q&A 목록 페이지로 리다이렉트합니다.
        return "redirect:/admin_QnA.do";
    }
		
	/*
	 *  관리자 1:1문의 게시판 페이지
	 */
	
	@GetMapping("/admin_ask.do")
	public String AskBoard(@ModelAttribute("loginUser") MemberData loginUser, Pageable pageable, Model model) {
		if (loginUser != null) {
            adminService.adminCheck(loginUser);
        } else {
        	return "main.do";    // 초기 로그인 화면으로 이동 **********************(수정필요)
        }
		
		Page<askBoard> askBoardPage = adminService.getAllAskBoardListWait(PageRequest.of(pageable.getPageNumber(), 10));
		
	    model.addAttribute("askBoardPage", askBoardPage);
			
		return "admin/boardPage/admin_askboard";  // 관리자 1:1문의 게시판으로 이동
	}
	
	@PostMapping("/admin_ask.do")
	public String AskBoardFinish(@ModelAttribute("loginUser") MemberData loginUser, RedirectAttributes redirectAttributes, Pageable pageable, Model model) {
		if (loginUser != null) {
            adminService.adminCheck(loginUser);
        } else {
        	return "main.do";    // 초기 로그인 화면으로 이동 **********************(수정필요)
        }
		
		Page<askBoard> askBoardPage = adminService.getAllAskBoardListFinish(PageRequest.of(pageable.getPageNumber(), 10));
		
		model.addAttribute("askBoardPage", askBoardPage);
		redirectAttributes.addFlashAttribute("askBoardPage", askBoardPage);
			
		return "redirect:/admin_ask_board.do";
	}
	
	@GetMapping("/admin_ask_board.do")
	public String showFinishAsk(@ModelAttribute("loginUser") MemberData loginUser) {
	    // 필요한 경우 추가 로직을 구현
	    return "admin/boardPage/admin_askboardEnd";  // 관리자 1:1문의 게시판 종료된 문의를 보여주는 페이지
	}
	
	@GetMapping("/askDetail.do")
	public String askDetail(@ModelAttribute("loginUser") MemberData loginUser, @RequestParam("askNum") int askNum, Model model) {
		if (loginUser != null) {
            adminService.adminCheck(loginUser);
        } else {
        	return "redirect:/admin/admin_main";    // 초기 로그인 화면으로 이동 **********************(수정필요)
        }
		
		askBoard ask = adminService.getByAskBoardnum(askNum);
		model.addAttribute("ask", ask);
		model.addAttribute("end_view", "view");
		
		return "admin/boardForm/askEditForm";
	}
	
	@GetMapping("/askEdit.do")
	public String askEdit(@ModelAttribute("loginUser") MemberData loginUser, @RequestParam("askNum") int askNum, Model model) {
		if (loginUser != null) {
            adminService.adminCheck(loginUser);
        } else {
        	return "redirect:/admin/admin_main";    // 초기 로그인 화면으로 이동 **********************(수정필요)
        }
		
		askBoard ask = adminService.getByAskBoardnum(askNum);
		model.addAttribute("ask", ask);
		
		return "admin/boardForm/askEditForm";
		
	}
	
	@PostMapping("/askUpdate.do")
	public void askUpdate(@ModelAttribute("loginUser") MemberData loginUser, @RequestParam("askNum") Long boardnum, 
			askBoard vo) {
		askBoard ask = adminService.getByAskBoardnum(boardnum);
		vo.setRegdate(ask.getRegdate());
		vo.setInquiry_id(boardnum);
		vo.setEmail(ask.getEmail());
		vo.setName(ask.getName());
		vo.setStatus("답변 완료");
		vo.setMember_data(loginUser);
		
		adminService.adminCheck(loginUser);
		adminService.updateAdminInquiry(vo);
	}
	
	
	/*
	 * 관리자 DB관리 페이지
	 */
	
	@GetMapping("/recipeReg.do")
	public String recipeReg(@ModelAttribute("loginUser") MemberData loginUser, @RequestParam("foodSelect") int boardnum, Model model) {
		adminService.adminCheck(loginUser);
		
		AdminRecipeBoard vo = adminService.getByRecipeBoardnum(boardnum);
		model.addAttribute("recipe", vo);
		
		return "admin/boardForm/recipeReg";
	}
	
	@PostMapping("/insertDB.do")
    public String insertRecipe(@ModelAttribute("loginUser") MemberData loginUser, foodRecipe vo) {
        // boardnum에 해당하는 Q&A 게시글을 삭제합니다.
		adminService.adminCheck(loginUser);
		
        adminService.insertAdminRecipeDB(vo);

        // 삭제 후 Q&A 목록 페이지로 리다이렉트합니다.
        return "redirect:/admin_food.do";
    }
	
	@GetMapping("/dataManager.do")
	public String dataManager(@ModelAttribute("loginUser") MemberData loginUser, HttpServletRequest request, Model model) {
		// loginUser가 존재하는지 확인하고 adminCheck 수행
        if (loginUser != null) {
            adminService.adminCheck(loginUser);
        } else {
        	return "admin/admin_main";    // 초기 로그인 화면으로 이동 **********************(수정필요)
        }
        
     // "dbList"와 "message"는 RedirectAttributes를 통해 자동으로 포함될 것이므로 별도의 처리가 필요 없음
        if (!model.containsAttribute("dbList")) {
            model.addAttribute("dbList", new ArrayList<foodRecipe>());
        }
        if (!model.containsAttribute("message")) {
            model.addAttribute("message", "");
        }
        
        return "admin/boardPage/admin_db";
		
	}
	
	@PostMapping("/getSearch.do")
	public String getSearch(@ModelAttribute("loginUser") MemberData loginUser, @RequestParam("searchField") String searchField, @RequestParam("searchWord") String searchWord, RedirectAttributes redirectAttributes, Model model) {
		// loginUser가 존재하는지 확인하고 adminCheck 수행
        if (loginUser != null) {
            adminService.adminCheck(loginUser);
        } else {
        	return "admin/admin_main";    // 초기 로그인 화면으로 이동 **********************(수정필요)
        }
        
        List<foodRecipe> dbList;
        
        if (searchField.equals("index")) {
            dbList = adminService.getRecipeDBListByIndex(searchWord);
        } else {
        	if(searchWord.equals("")) {
        		searchWord=null;
        	}
            dbList = adminService.getRecipeDBListByName(searchWord);
        }
        
        model.addAttribute("dbList", dbList);
        
        redirectAttributes.addFlashAttribute("dbList", dbList);
        redirectAttributes.addFlashAttribute("message", dbList.isEmpty() ? "검색 결과가 조회되지 않습니다." : "");

        
        return "redirect:/dataManager.do";  // 검색 결과를 포함한 페이지로 이동
	}
	
	@PostMapping("/dbDelete.do")
    public String deleteDB(@ModelAttribute("loginUser") MemberData loginUser, @RequestParam("dbidx") int dbidx) {
		if (loginUser != null) {
            adminService.adminCheck(loginUser);
        } else {
        	return "admin/admin_main";    // 초기 로그인 화면으로 이동 **********************(수정필요)
        }
        adminService.deleteDBData(dbidx);

        // 삭제 후 Q&A 목록 페이지로 리다이렉트합니다.
        return "redirect:/admin_QnA.do";
    }
	
	@GetMapping("/dbEdit.do")
	public String dbEdit(@ModelAttribute("loginUser") MemberData loginUser, @RequestParam("dbidx") int dbidx, Model model) {
		if (loginUser != null) {
            adminService.adminCheck(loginUser);
        } else {
        	return "redirect:/admin/admin_main";    // 초기 로그인 화면으로 이동 **********************(수정필요)
        }
		
		foodRecipe recipe = adminService.getRecipeDBByIndex(dbidx);
		model.addAttribute("recipe", recipe);
		
		return "admin/boardForm/dbEdit";
		
	}
	
	@PostMapping("/updateDB.do")
	public void dbUpdate(@ModelAttribute("loginUser") MemberData loginUser, foodRecipe vo) {
		
		adminService.adminCheck(loginUser);
		adminService.updateAdminRecipeDB(vo);
		
	}
}
