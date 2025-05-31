package com.demo.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.demo.domain.Member;
import com.demo.domain.OrderStatus;
import com.demo.domain.Store;
import com.demo.dto.ItemDTO;
import com.demo.dto.SearchResultDTO;
import com.demo.service.AladinApiService;
import com.demo.service.StoreService;

import jakarta.servlet.http.HttpSession;

@Controller
public class StoreController {

	@Autowired
	private StoreService storeService;
	@Autowired
	private AladinApiService aladinService;
	
	// 구매 페이지
	@GetMapping("/order")
	public String orderMain(HttpSession session) {
		return "order/orderMain";
	}
	@GetMapping("/order-form")
	public String orderPage(@RequestParam String title, @RequestParam String author,
							@RequestParam String imageUrl, Model model) {
		model.addAttribute("title", title);
		model.addAttribute("author", author);
		model.addAttribute("imageUrl", imageUrl);
		return "order/orderform";
	}
	// 구매 내역 저장
	@PostMapping("/order-action")
	public String orderAction(Store vo, Model model, HttpSession session,
			@RequestParam("coverImageUrl") String coverImageUrl) {
		Member loginUser = (Member)session.getAttribute("loginUser");
		if(loginUser == null) {
			model.addAttribute("message", "로그인 필요");
			model.addAttribute("text", "도서 구매는 로그인 후 이용 가능합니다.");
			return "login/login";
		}
		vo.setMember(loginUser);
		
//		if (coverImageUrl != null && !coverImageUrl.isEmpty()) {
//			List<String> fileUrls = new ArrayList<>();
//			fileUrls.add(coverImageUrl);
//			vo.setImageUrl(coverImageUrl);
//		}
		storeService.insertOrder(vo);
		return "redirect:/order";
	}
	
	// 구매 상태 수정 (관리자 전용)
	@PostMapping("/update-orderstatus")
	public ResponseEntity<String> updateOrderStatus(HttpSession session, @RequestParam int storeseq,
			@RequestParam OrderStatus status) {
		Member admin = (Member) session.getAttribute("admin");

		if (admin == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 후 사용할 수 있는 기능입니다.");
		}

		try {
			storeService.updateStatus(storeseq, status);
			return ResponseEntity.ok("구매 상태가 성공적으로 수정되었습니다.");
		} catch (IllegalAccessException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 주문 내역을 찾을 수 없습니다. (id: " + storeseq + ")");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
		}
	}

	// 도서 구매 검색
	@GetMapping("/order-search")
	public String searchBooks(@RequestParam("query") String query, 
	                          @RequestParam("searchTarget") String searchTarget,
	                          @RequestParam(value = "sort", defaultValue = "Accuracy") String sort, 
	                          Model model,
	                          @RequestParam(value = "page", defaultValue = "1") int page) {

	    SearchResultDTO aladinBook = aladinService.searchBooks(query, searchTarget, sort, page);

	    List<ItemDTO> books = aladinBook.getItems();
	    int totalResults = aladinBook.getTotalResults();
	    int totalPages = (int) Math.ceil(totalResults / 10.0);
	    totalPages = Math.min(totalPages, 50); // 최대 50페이지 제한

	    System.out.println("totalResults from API: " + totalResults);
	    
	    model.addAttribute("books", books);
	    model.addAttribute("query", query);
	    model.addAttribute("searchTarget", searchTarget);
	    model.addAttribute("sort", sort);
	    model.addAttribute("page", page);
	    model.addAttribute("totalPages", totalPages);
	    
	    // 페이지가 마지막 페이지일 경우 다음 링크를 숨기기 위한 논리
	    model.addAttribute("hasNextPage", page < totalPages);
	    
	    return "order/orderMain";
	}

	// 도서 구매
	@GetMapping("/order-write-form2")
	public String recommendOrderForm(Model model, @RequestParam("title") String title,
			@RequestParam("author") String author, @RequestParam("cover") String cover,
			@RequestParam("priceSales") int pricesales, @RequestParam("priceStandard") int pricestandard,
			HttpSession session) {
		Member loginUser = (Member)session.getAttribute("loginUser");
		if(loginUser == null) {
			model.addAttribute("message", "로그인 페이지로 이동");
			model.addAttribute("text", "도서 구매는 로그인 후 이용 가능합니다.");
			model.addAttribute("messageType", "info");
			
			return "login/login";
		}
		
		model.addAttribute("loginUser", loginUser);
		model.addAttribute("title", title);	// 책 제목
		model.addAttribute("author", author);	// 책 저자
		model.addAttribute("cover", cover);	// 책 판매가
		model.addAttribute("priceSales", pricesales);	// 책 정가
		model.addAttribute("priceStandard", pricestandard);	// 책 이미지
		
		return "order/orderform2";
	}
	@PostMapping("/order/submit")
	public String submitOrder(Store vo, HttpSession session, Model model, @RequestParam String title,
			@RequestParam String author, @RequestParam int totalPrice, @RequestParam String address,
			@RequestParam int ea, @RequestParam String imageUrl) {
		Member loginUser = (Member)session.getAttribute("loginUser");
		
		if(loginUser == null) {
			model.addAttribute("message", "로그인 페이지로 이동");
			model.addAttribute("text", "도서 구매는 로그인 후 이용 가능합니다.");
			model.addAttribute("messageType", "info");
			
			return "login/login";
		}
		vo.setMember(loginUser); // 로그인한 사용자 정보를 리뷰에 설정
		vo.setTitle(title);
		vo.setAddress(address);
		vo.setAuthor(author);
		vo.setEA(ea);
		vo.setImageUrl(imageUrl);
		vo.setPrice(totalPrice);
		storeService.insertOrder(vo);
		return "redirect:/order";
	}
}
