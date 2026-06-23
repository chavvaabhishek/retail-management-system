package com.rm.controller;

import com.rm.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestEmailController {

    private final EmailService emailService;

    @GetMapping("/email")
    public String testEmail() {

        emailService.sendEmail(
                "chavvaabhishek20@gmail.com",
                "Retail Test",
                "Email service working"
        );
        return "Email Sent";
    }




}