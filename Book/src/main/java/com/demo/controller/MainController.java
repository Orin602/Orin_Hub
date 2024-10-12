package com.demo.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.demo.domain.Member;
import com.demo.domain.SearchHistory;
import com.demo.dto.BookRecommendation;
import com.demo.dto.ItemDTO;
import com.demo.dto.SearchResultDTO;
import com.demo.service.BookRecommendationService;
import com.demo.service.SearchHistoryService;
import com.demo.service.SearchService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpSession;
import jakarta.xml.bind.JAXBException;

@Controller
public class MainController {
	
	@Autowired
	private BookRecommendationService bookRecoService;
	@Autowired
	private SearchHistoryService searchHisService;
	@Autowired
	private SearchService searchService;
	@Autowired
    private ObjectMapper objectMapper;
	
	// 초기 화면
	@GetMapping("/")
	public String indexPage() {
		return "index";
	}

	// 메인 화면
	@GetMapping("/main")
	public String mainPage(HttpSession session, Model model, @ModelAttribute("message") String message,
            @ModelAttribute("text") String text, @ModelAttribute("messageType") String messageType) {
		Member loginUser = (Member) session.getAttribute("loginUser");
		
		if (loginUser == null) {
			return "main";
		}

		// 플래시 메시지를 모델에 추가 (필요한 경우)
	    if (message != null) {
	        model.addAttribute("message", message);
	        model.addAttribute("text", text);
	        model.addAttribute("messageType", messageType);
	    }
		
		return "main";
	}
	
	// 사서 추천 도서
	@GetMapping("/fetch-recommendations")
    @ResponseBody
    public Map<String, Object> fetchRecommendations(@RequestParam String start_date, @RequestParam String end_date) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<BookRecommendation> recommendations = bookRecoService.getBookRecommendations(start_date, end_date);
         
            // 총 개수를 response에 추가
            response.put("totalCount", recommendations.size());
            
            if (recommendations.isEmpty() || (recommendations.size() == 1 && recommendations.get(0).getRecomtitle() == null)) {
                response.put("message", "아직 이달의 사서 도서 추천이 없습니다.");
            } else {
                response.put("recommendations", recommendations);
            }
        } catch (JAXBException e) {
            response.put("error", "Failed to fetch book recommendations.");
        }
        return response;
    }
	
	// 도서 검색
	@GetMapping("/search-book")
	public String searchBooks(@RequestParam("query") String query, HttpSession session,
            @RequestParam(value = "page", defaultValue = "1") int page, Model model) {
		Member loginUser = (Member) session.getAttribute("loginUser");
		SearchHistory searchHistory = new SearchHistory();
		
		
		if(loginUser != null) {
			searchHistory.setMember(loginUser);
		}
		
		searchHistory.setQuery(query);
		searchHistory.setSearch_date(new Date());
		
		searchHisService.saveSearchHistory(searchHistory);
		
		// 검색 서비스 호출
        List<ItemDTO> items = searchService.searchBooks(query, page);
        // 검색 결과를 세션에 저장
        session.setAttribute("searchResults", items);
        // 모델에 검색 결과 추가
        model.addAttribute("items", items);
        System.out.println("Items: " + items);
        
        // 검색어를 모델에 추가하여 검색어를 유지하도록 함
        model.addAttribute("query", query);

        // 결과를 보여줄 HTML 템플릿 이름
        return "searchBook/searchBookMain";
    }
}
