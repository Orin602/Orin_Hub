package com.demo.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.demo.domain.Book;
import com.demo.domain.MemberData;
import com.demo.service.BookService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@Controller
public class BookController {
	
	@Autowired
	BookService booksv;
	
	@PersistenceContext
    private EntityManager entityManager;
	
	@GetMapping("/book")
	public String goBookpage(HttpSession session) {
		 MemberData loginUser = (MemberData) session.getAttribute("loginUser");
		 
		if (loginUser == null) { 
			return "loginForm"; 
		}
		return "Book/Book";
	}
	
	// 예약리스트
	@PostMapping("/Book_List")
	public String getMyBookList(HttpSession session,
	        @RequestParam(value = "bookseq", defaultValue = "1") int bookseq,
	        @RequestParam(value = "page", defaultValue = "1") int page,
	        @RequestParam(value = "size", defaultValue = "6") int size, Model model) {
	    
	    MemberData loginUser = (MemberData) session.getAttribute("loginUser");
	    int id = (int) loginUser.getNo_data();
	    Page<Book> pageList = booksv.getAllBook(bookseq, page, size, id);
	    
	    long totalElements = pageList.getTotalElements();
	    long startNumber = totalElements - (page - 1) * size;
	    
	    model.addAttribute("BookList", pageList.getContent());
	    model.addAttribute("totalPages", pageList.getTotalPages());
	    model.addAttribute("pageNumber", page);
	    model.addAttribute("startNumber", startNumber);
	    
	    return "Book/BookList";
	}


	
	
	// 예약 상세정보 조회
		@PostMapping("/Book_detail")
		public String getCom_Board_DetailView(@RequestParam("bookseq") int bookseq, Model model) {
			Book vo = booksv.getBook(bookseq);		    
			model.addAttribute("BookVO", vo);
			return "Book/BookResult";
		}
		

	    // 예약전송
		@Transactional
		@PostMapping("/Bookwritesubmit")
		@ResponseBody
		public ResponseEntity<Map<String, Object>> insertBook(
		        HttpSession session, Model model,
		        @RequestBody Map<String, Object> bookData) {

		    MemberData loginUser = (MemberData) session.getAttribute("loginUser");
		    loginUser = entityManager.merge(loginUser);

		    Book book = new Book();
		    book.setMember_data(loginUser);

		    // 문자열을 java.util.Date로 변환
		    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		    try {
		        Date bookdateS = dateFormat.parse(bookData.get("bookdateS").toString());
		        Date bookdateE = dateFormat.parse(bookData.get("bookdateE").toString());
		        book.setBookdateS(bookdateS);
		        book.setBookdateE(bookdateE);
		    } catch (ParseException e) {
		        e.printStackTrace();
		        return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Invalid date format"));
		    }

		    book.setCampingname(bookData.get("campingname").toString());
		    book.setCampingid(bookData.get("campingid").toString());
		    book.setHeadcount(bookData.get("headcount").toString());
		    book.getMember_data().setName(bookData.get("bookname").toString());
		    book.getMember_data().setTelephone(bookData.get("phone").toString());
		    book.setMessage(bookData.get("message").toString());

		    booksv.insertBook(book);

		    Map<String, Object> response = new HashMap<>();
		    response.put("message", "예약이 성공적으로 완료되었습니다.");
		    response.put("bookseq", book.getBookseq());

		    return ResponseEntity.ok(response);
		}

		
		 

	    
	 // 캠핑장명으로 검색
		@GetMapping("/Book_search")
		public String getSearchByType(@RequestParam(value = "bookseq", defaultValue = "1") int bookseq,
				@RequestParam("searchKeyword") String keyword,
				@RequestParam(value = "page", defaultValue = "1") int page,
				@RequestParam(value = "size", defaultValue = "6") int size, Model model) {

			Page<Book> pageList;
			
		    pageList = booksv.getBookByCamping(bookseq, page, size, keyword);

		    List<Book> searchResult = pageList.getContent();
		    long totalElements = pageList.getTotalElements();
		    long startNumber = totalElements - (page - 1) * size;

		    model.addAttribute("BookList", searchResult);
		    model.addAttribute("totalPages", pageList.getTotalPages());
		    model.addAttribute("pageNumber", page);
		    model.addAttribute("startNumber", startNumber);

			return "Book/BookList";
		}
		
		
		// 예약수정 페이지로 이동
		@GetMapping("/Book_update")
		public String getBookUpdate(HttpSession session, Model model, @RequestParam("bookseq") int bookseq) {
			MemberData loginUser =  (MemberData)session.getAttribute("loginUser");
			Book BookVO = booksv.getBook(bookseq);

				if (loginUser == null) { 
					return "loginForm"; 
				} else if(!(loginUser.getId()).equals(BookVO.getMember_data().getId())){
					return "본인이 작성한 글만 수정가능합니다.";
				}else {	
			        model.addAttribute("BookVO", BookVO);
			        }	
					return "Book/BookUpdate";
				
		}
		
		
		// 예약수정
		@Transactional
		@PostMapping("/Bookupdatesubmit")
		@ResponseBody
		public ResponseEntity<Map<String, Object>> updateBook(
		        HttpSession session, Model model, @RequestBody Map<String, Object> bookData) {

		    MemberData loginUser = (MemberData) session.getAttribute("loginUser");
		    if (loginUser != null) {
		        loginUser = entityManager.merge(loginUser);
		    }

		    
		    int bookseq = (int) bookData.get("bookseq");
		    
		    // 기존 책 정보
		    Book book = booksv.getBook(bookseq);
		    if (book == null) {
		        return ResponseEntity.badRequest().body(Collections.singletonMap("message", "해당 책 정보를 찾을 수 없습니다."));
		    }

		    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		    try {
		        Date bookdateS = dateFormat.parse(bookData.get("bookdateS").toString());
		        Date bookdateE = dateFormat.parse(bookData.get("bookdateE").toString());
		        book.setBookdateS(bookdateS);
		        book.setBookdateE(bookdateE);
		    } catch (ParseException e) {
		        e.printStackTrace();
		        return ResponseEntity.badRequest().body(Collections.singletonMap("message", "날짜 변환 실패했습니다"));
		    }


		    book.setHeadcount(bookData.get("headcount").toString());
		    book.getMember_data().setName(bookData.get("bookname").toString());
		    book.getMember_data().setTelephone(bookData.get("phone").toString());
		    book.setMessage(bookData.get("message").toString());

		   
		    booksv.updateBook(book);

		    Map<String, Object> response = new HashMap<>();
		    response.put("message", "예약이 수정되었습니다");
		    response.put("bookseq", book.getBookseq());
		    response.put("redirectUrl", "/Book_detail?bookseq=" + book.getBookseq());

		    return ResponseEntity.ok(response);
		}



		
		
		// 예약취소
		@PostMapping("/Book_delete")
		public String deleteBook(@RequestParam(value = "bookseq") int bookseq,
				RedirectAttributes redirectAttributes) {
		    Book vo = booksv.getBook(bookseq);
		    if (vo != null) {
		        booksv.deleteBook(vo);
		        redirectAttributes.addFlashAttribute("message", "예약이 성공적으로 취소되었습니다.");
		    } else {
		        redirectAttributes.addFlashAttribute("message", "예약 취소에 실패했습니다.");
		    }
		    return "redirect:/book";
		}
}
	
