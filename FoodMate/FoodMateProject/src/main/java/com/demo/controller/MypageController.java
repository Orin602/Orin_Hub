package com.demo.controller;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.demo.domain.Com_Board_Detail;
import com.demo.domain.MemberData;
import com.demo.domain.Recommend_History;
import com.demo.domain.WeightRecord;
import com.demo.service.Com_Board_DetailService;
import com.demo.service.MemberService;
import com.demo.service.Recommend_HistoryService;
import com.demo.service.WeightRecordService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MypageController {

	@Autowired
	private MemberService memberService;
	@Autowired
	private Com_Board_DetailService boardService;
	@Autowired
	private Recommend_HistoryService recommendService;
	@Autowired
	private WeightRecordService recordService;

	
	// 마이페이지 메인화면
	@GetMapping("/mypage")
	public String mypageView(HttpSession session, Model model) {
		MemberData loginUser = (MemberData)session.getAttribute("loginUser");
		
		if(loginUser == null) {
			return "redirect:/login";
		} 
			return "mypage/mypageMain";
	}
	
	// 내 정보 화면
	@GetMapping("/infoView")
	public String myInfoView(HttpSession session, Model model) {
		MemberData loginUser = (MemberData)session.getAttribute("loginUser");
		
		if(loginUser == null) {
			return "redirect:/login";
		} else {
			 // 최신 사용자 정보를 데이터베이스에서 가져오기
	        MemberData userInfo = memberService.getMember(loginUser.getId());

	        // 정보를 Thymeleaf 템플릿에 전달
	        model.addAttribute("name", userInfo.getName());
	        model.addAttribute("id", userInfo.getId());
	        model.addAttribute("********", "********");
	        model.addAttribute("email", userInfo.getEmail());
	        model.addAttribute("nickname", userInfo.getNickname());
	        model.addAttribute("gender", userInfo.getGender());
	        model.addAttribute("age", userInfo.getAge());
	        model.addAttribute("height", userInfo.getHeight());
	        model.addAttribute("weight", userInfo.getWeight());
	        model.addAttribute("goal", userInfo.getGoal());
	        model.addAttribute("bmi", userInfo.getBmi());
		}
		return "mypage/infoView";
	}
	
	// 내 정보 수정 화면
	@GetMapping("/infoUpdate")
	public String infoUpdateView(HttpSession session, Model model) {
		MemberData loginUser = (MemberData)session.getAttribute("loginUser");
		
		if(loginUser == null) {
			return "redirect:/login";
		} else {
			// 최신 사용자 정보를 데이터베이스에서 가져오기
	        MemberData userInfo = memberService.getMember(loginUser.getId());
	        
			model.addAttribute("loginUser", userInfo);
			
		}
		return "mypage/infoUpdate";
	}
	
	// 개인정보 수정
	@PostMapping("/update_info")
	public String infoUpdateAction(HttpSession session, MemberData vo) {
	    MemberData loginUser = (MemberData) session.getAttribute("loginUser");
	    
	    if(loginUser == null) {
			return "redirect:/login";
		} else {
			// 로그인한 회원수정
			memberService.changeInfo(vo);
			
		}
	    return "redirect:/infoView";
	}

	
	// 닉네임 중복 확인 처리
	@GetMapping("/nickname_check_form")
	public String nicknameCheckView(MemberData vo, Model model) {
		int result = memberService.confirmNickname(vo.getNickname());
		
		model.addAttribute("message", result);
		model.addAttribute("nickname", vo.getNickname());
		
		return "mypage/nickcheck";
	}
	
	// 바디데이터 수정 화면
	@GetMapping("bodyUpdate")
	public String bodyUpdateView(HttpSession session, Model model) {
		MemberData loginUser = (MemberData)session.getAttribute("loginUser");
		
		if(loginUser == null) {
			return "redirect:/login";
		} else {
			// 최신 사용자 정보를 데이터베이스에서 가져오기
	        MemberData userInfo = memberService.getMember(loginUser.getId());
	        
			model.addAttribute("loginUser", userInfo);
			
		}
		return "mypage/bodyUpdate";
	}
	
	// 바디데이터 수정
	@PostMapping("/update_body")
	public String bodyUpdateAction(HttpSession session, MemberData vo) {
		MemberData loginUser = (MemberData)session.getAttribute("loginUser");
		
		if(loginUser == null) {
			return "redirect:/login";
		} else {
			// 로그인한 회원 바디데이터 수정
			vo.setId(loginUser.getId());
			memberService.changeBodyData(vo);
			
		}
		return "redirect:/infoView";
		
	}
	
	// 나의 체중 변화 그래프 화면
		@GetMapping("/myWeightChart")
		public String myWeightChartView(HttpSession session, Model model) {
			MemberData loginUser = (MemberData)session.getAttribute("loginUser");
			
			if(loginUser == null) {
				return "redirect:/login";
			} else {
				// 최신 사용자 정보를 데이터베이스에서 가져오기
		        MemberData userInfo = memberService.getMember(loginUser.getId());
		        
		        // 최근 7일과 30일의 체중 기록을 가져옴
	            List<WeightRecord> recentWeekRecords = recordService.getRecentWeekRecords(loginUser.getId());
	            List<WeightRecord> recentMonthRecords = recordService.getRecentMonthRecords(loginUser.getId());

		        // 주간 변화 평균값 계산
	            double weeklyAvg = recordService.calculateAverageWeight(recentWeekRecords);

	            // 월간 변화 평균값 계산
	            double monthlyAvg = recordService.calculateAverageWeight(recentMonthRecords);

	            // DecimalFormat을 사용하여 소수점 2자리까지 형식화
	            DecimalFormat df = new DecimalFormat("#.##");
	            weeklyAvg = Double.parseDouble(df.format(weeklyAvg));
	            monthlyAvg = Double.parseDouble(df.format(monthlyAvg));
	            
		        model.addAttribute("goal", userInfo.getGoal());
		        model.addAttribute("weekly", weeklyAvg);
		        model.addAttribute("monthly", monthlyAvg);
		        
			}
			return "mypage/myWeightChart";
		}
	
		// 체중변화 값 저장하기
		@PostMapping("/weight_record")
		@ResponseBody
		public ResponseEntity<String> changeWeight(HttpSession session, @RequestParam("re_date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date reDate, 
		                           @RequestParam("re_weight") Double reWeight) {
		    MemberData loginUser = (MemberData) session.getAttribute("loginUser");
		    
		    if (loginUser == null) {
		        // 로그인되지 않은 경우 리다이렉트
		        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
		    } else {
		        WeightRecord weightRecord = new WeightRecord();
		        weightRecord.setRe_date(reDate);
		        weightRecord.setRe_weight(reWeight);
		        weightRecord.setMember(loginUser);
		        recordService.saveWeightRecord(weightRecord);
		        
		        // 마이페이지 내 몸무게 차트로 리다이렉트
		        return ResponseEntity.ok("/mypage/myWeightChart");
		    }
		}
		
		// 저장된 값들로 체중변화 차트 그리기
		@GetMapping("/getRecordChart")
		@ResponseBody
		public Map<String, Object> myWeightRecordChart(HttpSession session) {
		    Map<String, Object> response = new HashMap<>();
		    MemberData loginUser = (MemberData) session.getAttribute("loginUser");

		    if (loginUser == null) {
		        response.put("redirect", "/login");
		        return response;
		    } else {
		        // 조회한 값들 중 최근 일주일의 데이터를 전송
		        List<WeightRecord> recentWeekRecords = recordService.getRecentWeekRecords(loginUser.getId());
		        // 조회한 값들 중 최근 한달의 데이터를 전송
		        List<WeightRecord> recentMonthRecords = recordService.getRecentMonthRecords(loginUser.getId());

		        // 최근 일주일과 한달의 체중 기록을 Map에 담아 반환
		        response.put("weeklyData", recentWeekRecords);
		        response.put("monthlyData", recentMonthRecords);
		        return response;
		    }
		}
		
	// 나의 레시피 화면
	@GetMapping("/myRecipeList")
	public String myRecipeListView(HttpSession session, Model model) {
		MemberData loginUser = (MemberData)session.getAttribute("loginUser");
		
		if(loginUser == null) {
			return "redirect:/login";
		} else {
			List<Com_Board_Detail> recipeList = boardService.getMyRecipe(loginUser.getId());
			
			model.addAttribute("recipeList", recipeList);
			
		}
		return "mypage/myRecipeList";
	}
	
	@GetMapping("/foodRecommend")
	public String foodRecommendView(HttpSession session, Model model) {
	    MemberData loginUser = (MemberData) session.getAttribute("loginUser");

	    if (loginUser == null) {
	        return "redirect:/login";
	    } else {
	        List<Recommend_History> recommend = recommendService.getMyRecommendHistory(loginUser);

	        // Date 타입을 문자열로 변환하여 날짜별로 그룹화
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	        
	        // TreeMap을 사용하여 날짜순으로 그룹화
	        Map<String, List<Recommend_History>> groupedByDate = new TreeMap<>();
	        for (Recommend_History history : recommend) {
	            String dateKey = sdf.format(history.getRecommendDate());
	            groupedByDate.computeIfAbsent(dateKey, k -> new ArrayList<>()).add(history);
	        }

	        model.addAttribute("groupedByDate", groupedByDate);
	        model.addAttribute("recommend", recommend);

	    }
	    return "mypage/recommendHistory";
	}

	

	
	@PostMapping("/deleteHistory")
	@ResponseBody
	public String deleteHistory(@RequestBody List<Integer> deleteIdxArray, HttpSession session) {
	    // 세션에서 현재 로그인한 사용자 정보 가져오기
	    MemberData loginUser = (MemberData) session.getAttribute("loginUser");

	    // 로그인한 사용자의 추천 기록 중 선택된 항목 삭제
	    if (loginUser != null) {
	        for (int index : deleteIdxArray) {
	            recommendService.deleteRecommendHistory(index);
	        }
	        return "success";
	    } else {
	        return "fail";
	    }
	}
	
	
	
	
	
	
	
	
	
	
	
}