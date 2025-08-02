package com.example.demoShop.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    
    @Value("${spring.mail.username}")
    private String FROM_MAIL;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendPasswordResetEmail(String to, String resetToken) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        String resetLink = "http://localhost:3000/reset-password?token=" + resetToken; // React 페이지 URL
        String htmlContent = "<h3>비밀번호 재설정</h3>" +
                "<p>아래 링크를 클릭하여 비밀번호를 재설정하세요:</p>" +
                "<a href=\"" + resetLink + "\">비밀번호 재설정하기</a>" +
                "<p>링크는 15분 동안 유효합니다.</p>";

        helper.setTo(to);
        helper.setSubject("비밀번호 재설정 요청");
        helper.setText(htmlContent, true); // HTML 형식으로 전송
        helper.setFrom(FROM_MAIL);

        mailSender.send(message);
    }
}