package com.demo.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.demo.domain.MemberData;
import com.demo.dto.SelectedData;
import com.demo.service.RegionMapping;

import jakarta.servlet.http.HttpSession;

@RestController
public class HomeController {

    private static final String STD_IN = "stdin";
    private static final String STD_ERR = "stderr";

    @PostMapping("/processKeywords")
    public ResponseEntity<Map<String, String>> processKeywords(@RequestBody SelectedData selectedData, HttpSession session) {
        List<String> mappedDoNm = selectedData.getDoNm().stream()
                .map(RegionMapping::mapDoName)
                .collect(Collectors.toList());

        System.out.println("Selected Do: " + mappedDoNm);
        System.out.println("Selected Gu: " + selectedData.getGungu());
        System.out.println("Selected Faclt: " + selectedData.getFaclt());
        System.out.println("Selected Lct: " + selectedData.getLct());
        System.out.println("Selected Induty: " + selectedData.getInduty());
        System.out.println("Selected Bottom: " + selectedData.getBottom());
        System.out.println("Selected Sbrs: " + selectedData.getSbrs());

        Long loginUser = (Long) session.getAttribute("loginUserNumberData");

        
        List<String> encodedDoNm = null;

        if (loginUser != null && !loginUser.toString().isEmpty()) {
        	long userId = loginUser;  // loginUser 객체에서 userId를 가져옵니다.
            System.out.println("userId: " + userId);
        	
            // 파이썬 스크립트 실행
            String result = runningProcess(mappedDoNm, selectedData.getFaclt(), selectedData.getLct(), selectedData.getInduty(), selectedData.getBottom(), selectedData.getSbrs(), userId);

            // 결과 확인
            System.out.println(result);

            // 결과 파싱
            List<Integer> recommendations = new ArrayList<>();
            String[] tmpArr = result.split("\n");
            for (String line : tmpArr) {
                try {
                    recommendations.add(Integer.parseInt(line.trim()));
                } catch (NumberFormatException e) {
                    // 예외 발생 시 무시하고 진행
                }
            }

            System.out.println("추천 데이터 =>" + recommendations);
            session.setAttribute("recommendations", recommendations);

            
        }
        
        encodedDoNm = mappedDoNm.stream()
                .map(doNm -> {
                    try {
                        return URLEncoder.encode(doNm, StandardCharsets.UTF_8.toString());
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        return doNm;
                    }
                })
                .collect(Collectors.toList());

        String url = "/getSearchList?"
                + "doNm=" + (encodedDoNm != null ? String.join(",", encodedDoNm) : "")
                + "&gungu=" + String.join(",", selectedData.getGungu())
                + "&faclt=" + String.join(",", selectedData.getFaclt())
                + "&lct=" + String.join(",", selectedData.getLct())
                + "&induty=" + String.join(",", selectedData.getInduty())
                + "&bottom=" + String.join(",", selectedData.getBottom())
                + "&sbrs=" + String.join(",", selectedData.getSbrs())
                + "&page=" + 1
                + "&keyword=" + selectedData.getKeyword();

        Map<String, String> response = new HashMap<>();
        response.put("redirectUrl", url);
        return ResponseEntity.ok(response);
    }

    private static String runningProcess(List<String> doNm, List<String> faclt, List<String> lct, List<String> induty, List<String> bottom, List<String> sbrs, long userId) {
        Process process = null;
        File workingDirectory = new File("C:/Users/tiger/Kamp");
        String cmd = "python C:/Users/tiger/Kamp/recommend_Camp.py \"" + String.join(",", doNm) + "\" \"" + String.join(",", faclt) + "\" \"" + String.join(",", lct) + "\" \"" + String.join(",", induty) + "\" \"" + String.join(",", bottom) + "\" \"" + String.join(",", sbrs) + "\" \"" + userId + "\"";
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
