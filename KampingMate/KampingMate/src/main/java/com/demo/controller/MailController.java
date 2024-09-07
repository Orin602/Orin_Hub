package com.demo.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.demo.service.SHA256Util;
import com.demo.service.mailManager;

import jakarta.servlet.http.HttpSession;

@Controller
public class MailController {

    @Autowired
    private HttpSession session;

    private final mailManager mailManager;

    public MailController(mailManager mailManager) {
        this.mailManager = mailManager;
    }

    @PostMapping("/sendMail")
    @ResponseBody
    public String sendMail(@RequestParam String email) throws Exception {
        UUID uuid = UUID.randomUUID();
        String key = uuid.toString().substring(0, 7);
        String sub = "인증번호 입력을 위한 메일 전송";
        String con = "인증 번호: " + key;
        mailManager.send(email, sub, con);
        
        key = SHA256Util.getEncrypt(key, email);
        session.setAttribute("emailVerificationCode", key);
        return "인증 코드가 이메일로 전송되었습니다.";
    }

    @PostMapping("/checkMail")
    @ResponseBody
    public boolean checkMail(@RequestParam String insertKey, @RequestParam String email) throws Exception {
        String key = (String) session.getAttribute("emailVerificationCode");
        insertKey = SHA256Util.getEncrypt(insertKey, email);
        return key != null && key.equals(insertKey);
    }
}
