package com.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import jakarta.mail.Message.RecipientType;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;

@Component
public class mailManager {
    
    @Value("${spring.mail.username}")
    private String sender;
    
    @Autowired
    private JavaMailSender javaMailSender;
    
    public void send(String sendTo, String sub, String con) throws Exception {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        mimeMessage.setFrom(sender);
        mimeMessage.addRecipient(RecipientType.TO, new InternetAddress(sendTo));
        mimeMessage.setSubject(sub);
        mimeMessage.setText(con);
        javaMailSender.send(mimeMessage);
    }
}
