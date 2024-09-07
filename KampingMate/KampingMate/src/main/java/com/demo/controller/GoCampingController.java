package com.demo.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.demo.domain.GoCamping;
import com.demo.dto.CampingItem;
import com.demo.persistence.GoCampingRepository;
import com.demo.persistence.SearchHistoryRepository;
import com.demo.service.GoCampingAPI;
import com.demo.service.GoCampingService;
import com.demo.service.RegionMapping;
import com.demo.service.SearchHistoryService;
import com.demo.service.SigunguService;

import jakarta.servlet.http.HttpSession;

@Controller
public class GoCampingController {

    @Autowired
    private GoCampingAPI goCampingAPI;
    
    @Autowired
    private SigunguService sigunguService;
    
    @Autowired
    private GoCampingRepository gocampingRepo;
    
    @Autowired
    private SearchHistoryService searchHistoryService;

    @GetMapping("/campingSites")
    public String getCampingSites(@RequestParam(defaultValue = "1") int page, Model model, HttpSession session) {
        try {
            GoCampingAPI.CampingApiResponse response = goCampingAPI.getCampingSites(page, 100);
            List<CampingItem> campingItems = response.getItems();
            
            model.addAttribute("items", campingItems);
            model.addAttribute("loginUserNumberData", session.getAttribute("loginUserNumberData"));
            
            return "camping/campingSites";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Failed to load camping sites.");
            return "error";
        }
    }
    
    @GetMapping("/go_detail")
    public String goDetailPopupView(Model model) {
        List<String> do_list = gocampingRepo.findAllDoNmList();
        List<String> faclt_div_list = gocampingRepo.findAllFacltDivList();
        List<String> lct_cl_list = gocampingRepo.findAllLctClList();
        List<String> induty_list = gocampingRepo.findAllIndutyList();
        List<String> sbrs_cl_list = gocampingRepo.findAllSbrsClList();
        
        // 중복 제거 및 null 값 제거
        Set<String> uniqueDoList = new TreeSet<>(do_list.stream()
                                          .filter(item -> item != null)
                                          .map(RegionMapping::mapDoName)
                                          .collect(Collectors.toSet()));
        Set<String> uniqueFacltDivList = faclt_div_list.stream()
                                                       .filter(item -> item != null)
                                                       .collect(Collectors.toSet());
        Set<String> uniqueLctClList = lct_cl_list.stream()
                                                 .filter(item -> item != null)
                                                 .flatMap((String item) -> Arrays.stream(item.split(",")))
                                                 .map(String::trim)
                                                 .collect(Collectors.toSet());
        Set<String> uniqueIndutyList = induty_list.stream()
                                                  .filter(item -> item != null)
                                                  .flatMap((String item) -> Arrays.stream(item.split(",")))
                                                  .map(String::trim)
                                                  .collect(Collectors.toSet());

        // 주요 시설 중복 제거 및 null 값 제거
        Set<String> uniqueSbrsClList = sbrs_cl_list.stream()
                                                   .filter(item -> item != null)
                                                   .flatMap((String item) -> Arrays.stream(item.split(",")))
                                                   .map(String::trim)
                                                   .collect(Collectors.toSet());
        
        List<String> uniqueBottomClList = Arrays.asList("잔디", "파쇄석", "데크", "자갈", "맨흙");
        
        model.addAttribute("do_list", uniqueDoList);
        model.addAttribute("faclt_div_list", uniqueFacltDivList);
        model.addAttribute("lct_cl_list", uniqueLctClList);
        model.addAttribute("induty_list", uniqueIndutyList);
        model.addAttribute("site_bottom_list", uniqueBottomClList);
        model.addAttribute("sbrs_cl_list", uniqueSbrsClList);
        
        return "camping/detail_select";
    }
    
    @GetMapping("/getSearchList")
    public String getNormalSearchList(@RequestParam(name = "doNm", required = false) String doNmStr,
                                      @RequestParam(name = "gungu", required = false) String gunguStr,
                                      @RequestParam(name = "faclt", required = false) String facltStr,
                                      @RequestParam(name = "lct", required = false) String lctStr,
                                      @RequestParam(name = "induty", required = false) String indutyStr,
                                      @RequestParam(name = "bottom", required = false) String bottom,
                                      @RequestParam(name = "sbrs", required = false) String sbrsStr,
                                      @RequestParam(name = "keyword", required = false) String keyword,
                                      @RequestParam(defaultValue = "1") int page,
                                      @RequestParam(defaultValue = "10") int size,
                                      @RequestParam(defaultValue = "facltNm") String sortField,
                                      @RequestParam(defaultValue = "asc") String sortDirection,
                                      HttpSession session, Model model) {

        Set<GoCamping> resultSet = null;

        // Helper method to handle empty string checks and apply repository method
        BiFunction<String, Function<String, List<GoCamping>>, Set<GoCamping>> handleCondition =
            (str, func) -> {
                if (str != null && !str.trim().isEmpty()) {
                    Set<GoCamping> tempSet = new HashSet<>();
                    List<String> list = Arrays.asList(str.split(","));
                    for (String item : list) {
                        tempSet.addAll(func.apply("%" + item.trim() + "%")); // LIKE 연산자를 위한 와일드카드 추가
                    }
                    return tempSet;
                } else {
                    // str이 null이거나 공백일 경우 전체 목록 반환
                    return new HashSet<>(gocampingRepo.findAll());
                }
            };

        // 각 조건에 대해 개별 쿼리를 실행하고 결과를 합침
        resultSet = combineResults(resultSet, handleCondition.apply(doNmStr, gocampingRepo::findByDoNmLike));
        resultSet = combineResults(resultSet, handleCondition.apply(gunguStr, gocampingRepo::findBySigunguNmLike));
        resultSet = combineResults(resultSet, handleCondition.apply(facltStr, gocampingRepo::findByFacltLike));
        resultSet = combineResults(resultSet, handleCondition.apply(lctStr, gocampingRepo::findByLctClLike));
        resultSet = combineResults(resultSet, handleCondition.apply(indutyStr, gocampingRepo::findByIndutyLike));
        resultSet = combineResults(resultSet, handleCondition.apply(sbrsStr, gocampingRepo::findBySbrsClLike));
        
        // Keyword 검색을 추가합니다.
        if (keyword != null && !keyword.trim().isEmpty()) {
            Set<GoCamping> keywordResults = new HashSet<>(gocampingRepo.findByKeywordInAllColumns("%" + keyword.trim() + "%"));
            resultSet = combineResults(resultSet, keywordResults);
        }
        
        if (bottom != null && !bottom.trim().isEmpty()) {
            Set<GoCamping> bottomResults = new HashSet<>();
            List<String> bottomList = Arrays.asList(bottom.split(","));
            for (String b : bottomList) {
                bottomResults.addAll(gocampingRepo.findByBottomLike("%" + b.trim() + "%"));
            }
            resultSet = combineResults(resultSet, bottomResults);
        } else {
            // bottom이 null이거나 공백일 경우 전체 목록 반환
            resultSet = combineResults(resultSet, new HashSet<>(gocampingRepo.findAll()));
        }

        if (resultSet == null) {
            resultSet = new HashSet<>(gocampingRepo.findAll()); // 모든 결과를 조회합니다
        }

        // Convert Set to List and sort by content_id
        List<GoCamping> goCampingList = new ArrayList<>(resultSet);
        goCampingList.sort((a, b) -> {
            if ("asc".equals(sortDirection)) {
                return Integer.compare(a.getContentId(), b.getContentId());
            } else {
                return Integer.compare(b.getContentId(), a.getContentId());
            }
        });

        // Adjust page to be 0-based for subList
        int totalItems = goCampingList.size();
        int totalPages = (int) Math.ceil((double) totalItems / size);
        page = Math.max(1, page) - 1; // Ensure page is at least 1 and then adjust for 0-based index
        int start = Math.min(page * size, totalItems);
        int end = Math.min(start + size, totalItems);

        List<GoCamping> paginatedList = goCampingList.subList(start, end);

        // 조회된 캠핑장 리스트를 모델에 추가합니다.
        model.addAttribute("goCampingList", paginatedList);
        model.addAttribute("currentPage", page + 1); // Adjust back to 1-based index
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        
        List<Integer> recommendations = (List<Integer>) session.getAttribute("recommendations");
        model.addAttribute("recommendations", recommendations);
        
        if(recommendations != null) {
            List<GoCamping> recommendedCamps = gocampingRepo.findAllById(recommendations);
           
            model.addAttribute("recommend_list", recommendedCamps);
            } else {
               model.addAttribute("recommend_list", null);
            }

        return "camping/searchView"; // 뷰 페이지 이름을 반환합니다
    }


    private Set<GoCamping> combineResults(Set<GoCamping> mainSet, Set<GoCamping> newSet) {
        if (newSet != null) {
            if (mainSet == null) {
                return newSet;
            } else {
                mainSet.retainAll(newSet);
            }
        }
        return mainSet;
    }
    
    @GetMapping("/get_sigungu")
    @ResponseBody
    public List<String> getSigungu(@RequestParam String siDo) {
        String mappedSiDo = RegionMapping.mapDoName(siDo);
        Map<String, List<String>> sigunguMap = sigunguService.getSigunguData();
        return sigunguMap.getOrDefault(mappedSiDo, List.of());
    }
    
    @GetMapping("/regionMapping")
    @ResponseBody
    public Map<String, String> getRegionMapping() {
        return RegionMapping.DO_NAME_MAPPING;
    }
    
    @GetMapping("/detailView")
    public String goDetailView(@RequestParam("contentId") int contentId, Model model, HttpSession session) throws Exception {
        // contentId를 이용해 캠핑장 정보를 조회
        GoCamping campDetail = gocampingRepo.findById(contentId).orElse(null);
        Long no_data = (Long) session.getAttribute("loginUserNumberData");
        
        List<String> ImageUrlList = null;
        try {
        	ImageUrlList = goCampingAPI.getImageList(contentId);
        } catch (Exception e) {
            
        }
        
        // 캠핑장 정보를 모델에 추가
        model.addAttribute("campDetail", campDetail);
        model.addAttribute("imageList", ImageUrlList != null ? ImageUrlList : new ArrayList<>());
        
     // sbrsCl 필드를 쉼표로 분리하여 리스트로 변환
        if (campDetail != null && campDetail.getSbrsCl() != null) {
            List<String> facilities = Arrays.asList(campDetail.getSbrsCl().split(","));
            model.addAttribute("facilities", facilities);
        }
        
     // sbrsCl 필드를 쉼표로 분리하여 리스트로 변환
        if (campDetail != null && campDetail.getEqpmnLendCl() != null) {
            List<String> rentList = Arrays.asList(campDetail.getEqpmnLendCl().split(","));
            model.addAttribute("rentList", rentList);
        }
        
        if (no_data != null) {
            searchHistoryService.insertHistoryItem(contentId, no_data);
        }
        
        return "camping/detailView"; // detailView 템플릿을 반환
    }
    
    @PostMapping("/getRecommendList")
    public String getRecommendListView(HttpSession session, Model model) {
        // 세션에서 recommendations를 불러옴
        List<Integer> recommendations = (List<Integer>) session.getAttribute("recommendations");

        // 추천 캠핑장 목록 조회
        List<GoCamping> recommendedCamps = gocampingRepo.findAllById(recommendations);

        // 조회된 캠핑장 리스트를 모델에 추가
        model.addAttribute("goCampingList", recommendedCamps);

        return "camping/searchViewRecommend";
    }
    
}
