package com.rm.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Async
    public void sendEmail(
            String to,
            String subject,
            String body
    ) {

        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }


    @Async
    public void sendEmailWithAttachment(
            String to,
            String subject,
            String body,
            byte[] pdfBytes,
            String fileName
    ) {

        try {

            MimeMessage message =
                    mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(
                            message,
                            true
                    );

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body);

            helper.addAttachment(
                    fileName,
                    new ByteArrayResource(pdfBytes)
            );

            mailSender.send(message);

        } catch (Exception e) {

            throw new RuntimeException(
                    "Email sending failed"
            );
        }
    }
}