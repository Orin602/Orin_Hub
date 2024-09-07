package com.demo.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.demo.dto.KeywordData;
import com.demo.dto.RecommendData;

@RestController
public class HomeController {
	
	private static final String STD_IN = "stdin";
	private static final String STD_ERR = "stderr";
	
	@PostMapping("/processKeywords")
	public List<RecommendData> processKeywords(@RequestBody KeywordData keywordData) {
	    // 선택된 키워드와 제외할 재료를 받아서 처리
	    String selectedKeywords = keywordData.getSelectedKeywords();
	    String excludeIngredients = keywordData.getExcludeIngredients();
	    String dietCalory = (keywordData.getDietCalory()).replaceAll(" ", "");
	    String nutrientChoice = keywordData.getNutrientChoice();

	    System.out.println("selectedKeywords=" + selectedKeywords);
	    System.out.println("excludeIngredients=" + excludeIngredients);
	    System.out.println("dietCalory=" + dietCalory);
	    System.out.println("nutrientChoice=" + nutrientChoice);

	    List<RecommendData> foodList = new ArrayList<>();
	    String result = runningProcess(selectedKeywords, excludeIngredients, dietCalory, nutrientChoice);   // 파이썬 프로그램 실행

	    System.out.println("분석 결과=" + result);

	    String[] tmpArr = result.split("\n");
	    for (String line : tmpArr) {
	        String[] items = line.split("@");
	        if (items.length >= 4) { // Expected number of items
	            RecommendData food = new RecommendData();
	            food.setIdx(items[0].trim());
	            food.setName(items[1].trim());
	            food.setCalories(items[2].replace(" kcal", "").trim());
	            // 이미지 URL을 분리하여 처리
	            String[] imageUrls = items[3].split("\\.jpg");
	            for (int i = 0; i < imageUrls.length; i++) {
	                imageUrls[i] = imageUrls[i].trim() + ".jpg";
	            }

	            // 첫 번째 이미지 URL을 설정
	            if (imageUrls.length > 0) {
	                food.setImages(imageUrls[0]);
	            }

	            foodList.add(food);
	            System.out.println("food추가" + food);
	        } else {
	            System.out.println("Skipping line due to unexpected number of items: " + line);
	        }
	    }

	    return foodList;
	}

	private static String runningProcess(String selectedKeywords, String excludeIngredients, String dietCalory, String nutrientChoice) {
	    Process process = null;
	    File workingDirectory = new File("E:/Student/MachineLearning");
	    String cmd = "python E:/Student/MachineLearning/recommend_food.py \"" + selectedKeywords + "\" \"" + excludeIngredients + "\" " + dietCalory + " \"" + nutrientChoice + "\"";
	    ProcessStream processInStream = null;
	    ProcessStream processErrStream = null;
	    String result = "";

	    try {
	        process = Runtime.getRuntime().exec(cmd, null, workingDirectory);
	        processInStream = new ProcessStream(STD_IN, process.getInputStream());
	        processErrStream = new ProcessStream(STD_ERR, process.getErrorStream());

	        result = processInStream.start();
	        processErrStream.start();
	        process.getOutputStream().close();

	        process.waitFor();

	    } catch (IOException | InterruptedException e) {
	        e.printStackTrace();
	    }

	    return processInStream.getResult();
	}
}