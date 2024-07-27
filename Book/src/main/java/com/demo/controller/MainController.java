package com.demo.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.demo.domain.Member;
import com.demo.domain.SearchHistory;
import com.demo.dto.BookRecommendation;
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
	public String searchBookView(HttpSession session, Model model, @RequestParam("query") String query,
			@RequestParam(value = "start", defaultValue = "1") int start) {
		Member loginUser = (Member) session.getAttribute("loginUser");
		
		// 검색 기록 저장
        SearchHistory searchHistory = new SearchHistory();
        searchHistory.setQuery(query);
        searchHistory.setSearch_date(new Date());
        
		// 로그인한 사용자가 있는 경우, 검색 기록에 사용자 정보 설정
        if (loginUser != null) {
            searchHistory.setMember(loginUser);
        }
        
        // 검색 기록을 데이터베이스에 저장
        searchHisService.saveSearchHistory(searchHistory);
        
        // 검색어를 기반으로 API 호출
        SearchResultDTO searchResult = searchService.searchBooks(query, start);
        // JSON 문자열로 변환하여 모델에 추가
        try {
            String searchResultJson = objectMapper.writeValueAsString(searchResult);
            model.addAttribute("searchResultJson", searchResultJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            model.addAttribute("error", "검색 결과를 처리하는 동안 오류가 발생했습니다.");
        }

        model.addAttribute("query", query); // 페이지네이션에서 검색어를 유지하기 위해 추가
        model.addAttribute("start", start); // 페이지네이션에서 시작 페이지를 유지하기 위해 추가

        return "searchBook/searchBookMain";
	}
}
