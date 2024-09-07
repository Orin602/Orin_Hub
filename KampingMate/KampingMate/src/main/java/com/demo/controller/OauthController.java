package com.demo.controller;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.demo.domain.MemberData;
import com.demo.dto.GoogleUser;
import com.demo.service.MemberService;
import com.demo.service.OauthService;
import com.demo.service.PasswordGenerator;
import com.demo.persistence.*;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/oauth")
public class OauthController {

    private final OauthService oauthService;
    private final MemberService memberService;
    private final MemberRepository memberRepo;

    private String refreshToken;
    private String accessToken;

    

    @PostMapping("/google/callback")
    public String googleCallback(
            @RequestParam Map<String, String> params,
            HttpSession session) {
        log.info(">> 구글 소셜 로그인 API 서버로부터 받은 사용자 정보 :: {}", params);
        accessToken = params.get("access_token");
        refreshToken = params.get("refresh_token");
        GoogleUser user = new GoogleUser();
        user.setId(params.get("id"));
        user.setName(params.get("username"));
        user.setEmail(params.get("email"));
        user.setPicture(params.get("img"));
        user.setProvider(params.get("provider"));
        session.setAttribute("user", user);
        return "redirect:/oauth/contract"; // 약관 페이지로 리다이렉트
    }

    @GetMapping("/naver/callback")
    public String naverCallback(HttpSession session) {
        return "callback"; // 네이버 로그인 콜백 처리 페이지로 리다이렉트
    }

    @PostMapping("/saveUserSession")
    @ResponseBody
    public String saveUserSession(@RequestBody GoogleUser user, HttpSession session) {
        session.setAttribute("user", user);
        return "success";
    }

    @GetMapping("/contract")
    public String contractPage(HttpSession session, Model model) {
        GoogleUser user = (GoogleUser) session.getAttribute("user");
        if (user != null) {
            String id = user.getId();
            int exists = memberService.confirmID(id);
            if (exists == 1) {
                return "redirect:/oauth/autoLogin";
            }
            model.addAttribute("user", user);
        }
        return "contract"; // templates/contract.html로 이동
    }

    @GetMapping("/joinForm")
    public String joinFormPage(HttpSession session, Model model) {
        GoogleUser user = (GoogleUser) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("name", user.getName());
            model.addAttribute("email", user.getEmail());
            model.addAttribute("id", user.getId());
            model.addAttribute("provider", user.getProvider());
        }
        return "joinForm";
    }

    @PostMapping("/confirmEmail")
    @ResponseBody
    public int confirmEmail(@RequestParam String email) {
        return memberService.confirmEmail(email);
    }

    @PostMapping("/joinAction")
    public String joinAction(MemberData vo) {
        MemberData member = new MemberData();
        String randomPassword = PasswordGenerator.generateRandomPassword();

        // long 타입의 생년월일을 문자열로 변환
        String birthDateString = Long.toString(vo.getAge());

        // 문자열을 "yyyyMMdd" 형식으로 받아서 LocalDate 객체로 변환
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate birthDate = LocalDate.parse(birthDateString, formatter);

        // 현재 날짜
        LocalDate currentDate = LocalDate.now();

        // 나이 계산
        int age = Period.between(birthDate, currentDate).getYears();

        member.setId(vo.getId());
        member.setName(vo.getName());
        member.setAge((long) age);
        member.setEmail(vo.getEmail());
        member.setTelephone(vo.getTelephone());
        member.setProvider(vo.getProvider());
        member.setPassword(randomPassword);

        memberRepo.save(member);

        return "main";
    }

    @GetMapping("/autoLogin")
    public String autoLogin(HttpSession session) {
        GoogleUser user = (GoogleUser) session.getAttribute("user");
        if (user != null) {
            String id = user.getId();
            MemberData loginUser = memberService.getMember(id);
            Long loginUserNoData = (Long)loginUser.getNo_data();
            session.setAttribute("loginUserNumberData", loginUserNoData);
            session.setAttribute("loginUser", loginUser);
        }
        return "redirect:/main";
    }

    private ResponseEntity<String> getGoogleUserInfo() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange("https://www.googleapis.com/oauth2/v1/userinfo", HttpMethod.GET, entity, String.class);
    }
}
