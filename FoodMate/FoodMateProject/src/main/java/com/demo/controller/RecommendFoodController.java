package com.demo.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.domain.MemberData;
import com.demo.domain.Recommend_History;
import com.demo.domain.foodRecipe;
import com.demo.persistence.Recommend_HistoryRepository;
import com.demo.service.AdminService;
import com.demo.service.Recommend_HistoryService;

import jakarta.servlet.http.HttpSession;

@RestController
public class RecommendFoodController {

    @Autowired
    private AdminService foodService; // 서비스 또는 리포지토리 객체
    
    @Autowired
    private Recommend_HistoryService recommendHistoryService;
    
    @Autowired
    private Recommend_HistoryRepository rhrepo;
    
    @GetMapping("/getNutrientData")
    public ResponseEntity<Map<String, Object>> getNutrientData(@RequestParam("idx") int idx) {
        foodRecipe food = foodService.getFoodByIndex(idx); // 음식 데이터를 가져오는 서비스 메소드
        if (food != null) {
            Map<String, Object> nutrientData = new HashMap<>();
            nutrientData.put("idx", food.getIdx());
            nutrientData.put("name", food.getName());
            nutrientData.put("carbohydrates", food.getCarbohydratecontent());
            nutrientData.put("protein", food.getProteincontent());
            nutrientData.put("fat", food.getFatcontent());
            nutrientData.put("saturatedfat", food.getSaturatedfatcontent());
            nutrientData.put("cholesterol", food.getCholesterolcontent());
            nutrientData.put("sodium", food.getSodiumcontent());
            nutrientData.put("sugar", food.getSugarcontent());
            
            return ResponseEntity.ok(nutrientData);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    
    @GetMapping("/getRecipeData")
    public ResponseEntity<Map<String, Object>> getRecipeData(@RequestParam("idx") int idx) {
        foodRecipe food = foodService.getFoodByIndex(idx); // 음식 데이터를 가져오는 서비스 메소드
        if (food != null) {
            Map<String, Object> recipe = new HashMap<>();
            recipe.put("recipe", food.getRecipeinstructions());
            
            return ResponseEntity.ok(recipe);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    
    
    @GetMapping("/saveRecommendHistory")
    public ResponseEntity<String> saveRecommendHistory(@RequestParam int idx, HttpSession session) {
        MemberData loginUser = (MemberData) session.getAttribute("loginUser");

        if (loginUser != null) {
            try {
                // 사용자와 특정 음식에 대한 추천 이력 조회
                foodRecipe recommend_food = foodService.getFoodByIndex(idx);
                List<Recommend_History> existingHistories = rhrepo.findByMemberDataAndRecommendFood(loginUser, recommend_food);
                
                // 이미 추천 이력이 존재하는 경우
                if (!existingHistories.isEmpty()) {
                    // 가장 최근 추천 이력을 가져와서 날짜를 업데이트
                    Recommend_History existingHistory = existingHistories.get(0);
                    existingHistory.setRecommendDate(new Date());
                    recommendHistoryService.saveRecommendHistory(existingHistory);
                    return ResponseEntity.ok("Recommend history updated successfully.");
                } else {
                    // 새로운 추천 이력 생성
                    Recommend_History history = new Recommend_History();
                    history.setRecommendFood(recommend_food);
                    history.setMemberData(loginUser);
                    history.setRecommendDate(new Date());
                    recommendHistoryService.saveRecommendHistory(history);
                    return ResponseEntity.ok("Recommend history saved successfully.");
                }
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save or update recommend history.");
            }
        }
        return ResponseEntity.badRequest().body("FAILED");
}
}