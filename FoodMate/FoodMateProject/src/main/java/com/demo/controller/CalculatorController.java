package com.demo.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.demo.domain.MemberData;
import com.demo.domain.foodRecipe;
import com.demo.dto.CalculationResult;
import com.demo.persistence.AdminRecipeDBRepository;
import com.demo.persistence.Com_Board_DetailRepository;
import com.demo.persistence.MemberRepository;
import com.demo.persistence.SatisfactionRepository;
import com.demo.service.CalculatorServiceImpl;

import jakarta.servlet.http.HttpSession;


@SessionAttributes("loginUser")
@Controller
public class CalculatorController {

    private final CalculatorServiceImpl calculatorServiceImpl;
    private final MemberRepository memberRepository;
    private final AdminRecipeDBRepository adminRecipeDBRepository;
    private final Com_Board_DetailRepository comBoardDetailRepository;
    private final SatisfactionRepository satisfactionRepository;
    
    @Autowired
    public CalculatorController(CalculatorServiceImpl calculatorServiceImpl, MemberRepository memberRepository, AdminRecipeDBRepository adminRecipeDBRepository, Com_Board_DetailRepository comBoardDetailRepository, SatisfactionRepository satisfactionRepository) {
        this.calculatorServiceImpl = calculatorServiceImpl;
        this.memberRepository = memberRepository;
        this.adminRecipeDBRepository = adminRecipeDBRepository;
        this.comBoardDetailRepository = comBoardDetailRepository;
        this.satisfactionRepository = satisfactionRepository;
    }
    
    @GetMapping("/")
    public String homePage(Model model) {
        // 홈페이지에 필요한 데이터를 모델에 추가하고, 뷰 이름을 반환합니다.
    	model.addAttribute("welcomeMessage", "건강한 식단을 추천해드릴게요!");
    	// 회원 수 표시
    	int memberCount = memberRepository.getMemberCount();
    	model.addAttribute("member", memberCount);
    	// 총 레시피 표시
    	int recipeCount = comBoardDetailRepository.getRecipeCount();
    	model.addAttribute("recipe", recipeCount);
        
        int dbRecipeCount = adminRecipeDBRepository.getRecipeCount();
        model.addAttribute("dbrecipe", dbRecipeCount);
        
        int satisfiedCount = satisfactionRepository.getSatisFiedCount();
        int allCount = satisfactionRepository.getAllCount();
        
        int satisfiedPercentage = (int) Math.floor((double) satisfiedCount / allCount * 100);
        model.addAttribute("satisfiedPercentage", satisfiedPercentage);
        
        return "main"; // 여기서 "main"는 타임리프 템플릿 파일의 이름입니다.
    }


    @GetMapping("/UserChoice")
    public String showUserChoicePage(Model model, HttpSession session) {
        // 세션에서 사용자 정보를 가져옵니다.
        MemberData loggedInUser = (MemberData) session.getAttribute("loginUser");

        if (loggedInUser != null) {
            // 사용자 정보가 존재할 경우
        	MemberData member = memberRepository.findByLoginId(loggedInUser.getId());
        	if(member.getGender() == null || member.getGender().isEmpty() || member.getAge() == 0 || member.getWeight() == 0 || member.getHeight() == 0) {
        		return "redirect:/bodyUpdate";
        	} else {
        	
        	CalculationResult result = calculatorServiceImpl.calculate(member);
            model.addAttribute("member", member);
            model.addAttribute("result", result);
            return "foodRecommend/UserChoice";
        	}
        } else {
            // 로그인 페이지로 리다이렉트합니다.
            return "redirect:/login"; 
        }
    }
    
    @GetMapping("/go_searchList")
    public String goSearchView() {
        return "foodRecommend/SearchList";
    }
    
    @PostMapping("/getSearchKeywords")
    public String getSearchKeywords(@RequestParam("searchInput") String searchInput, Model model) {
        List<foodRecipe> list = adminRecipeDBRepository.getRecipeDBListByRecipeingredientparts(searchInput);
        Set<String> keywordSet = new HashSet<>(); // 중복을 허용하지 않는 Set을 사용하여 중복 제거

        for (foodRecipe recipe : list) {
            // 각 레시피의 키워드를 가져옴
            String keywords = recipe.getRecipeingredientparts();

            // 키워드가 null이 아니면서 소문자로 변환하여 검색어와 비교
            if (keywords != null) {
                String[] keywordArray = keywords.split(", "); // 쉼표로 구분된 키워드를 배열로 분할
                for (String keyword : keywordArray) {
                    // 검색어와 키워드를 모두 소문자로 변환하여 비교
                    if (keyword.toLowerCase().contains(searchInput.toLowerCase())) {
                        // 검색어가 포함된 경우 해당 키워드를 Set에 추가
                        keywordSet.add(keyword);
                    }
                }
            }
        }
        
        List<String> keywordList = new ArrayList<>(keywordSet); // Set을 List로 변환하여 중복 제거
        model.addAttribute("keywordList", keywordList);
        
        return "foodRecommend/SearchList";
    }
    
    @GetMapping("/RecommendedFood")
    public String showRecommendedFoodPage() {
        // 해당 페이지의 뷰 이름을 반환합니다. 뷰 이름은 실제 프로젝트 구조에 따라 결정됩니다.
        return "foodRecommend/RecommendedFood";
    }

}
