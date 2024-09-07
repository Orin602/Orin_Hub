package com.demo.controller;

import com.demo.service.NApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NApiController {

    @Autowired
    private NApiService nApiService;

    @GetMapping("/KampingNews")
    public String searchNews(Model model) {
        String responseBody = nApiService.searchNews("캠핑"); // 뉴스 검색어를 지정합니다.
        model.addAttribute("responseBody", responseBody);
        return "mypage/KampingNews";
    }
}
