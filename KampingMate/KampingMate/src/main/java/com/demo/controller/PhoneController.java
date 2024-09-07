package com.demo.controller;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;

@RestController
public class PhoneController {

    final DefaultMessageService messageService;

    @Autowired
    private HttpSession session;
    
    public PhoneController() {
        // 반드시 계정 내 등록된 유효한 API 키, API Secret Key를 입력해주셔야 합니다!
        this.messageService = NurigoApp.INSTANCE.initialize("NCSQQA6VHNZZBBJ9", "KJUDM9S3CHMIC8EJMXITQRS3PZDEH0C7", "https://api.coolsms.co.kr");
    }

    
    /**
     * 단일 메시지 발송 예제
     */
    @PostMapping("/send-one")
    public SingleMessageSentResponse sendOne(@RequestParam("phone") String phone) {
        String verificationCode = generateVerificationCode();
        session.setAttribute("verificationCode", verificationCode);

        Message message = new Message();
        // 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.
        message.setFrom("01038237821");
        message.setTo(phone);
        message.setText("Your verification code is: " + verificationCode);

        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
        System.out.println(response);

        return response;
    }

    /**
     * 인증 코드 확인 예제
     */
    @PostMapping("/verify-code")
    public String verifyCode(@RequestParam("code") String code) {
        String storedCode = (String) session.getAttribute("verificationCode");
        if (storedCode == null) {
            return "휴대전화 인증을 진행해주세요.";
        } else if (storedCode.equals(code)) {
            return "인증에 성공하였습니다";
        } else {
            return "인증에 실패하였습니다";
        }
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

}
