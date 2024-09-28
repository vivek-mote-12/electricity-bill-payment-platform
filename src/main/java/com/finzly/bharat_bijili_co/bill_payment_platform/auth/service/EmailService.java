package com.finzly.bharat_bijili_co.bill_payment_platform.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String emailAppPassword;

    public void sendOtpEmail(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailAppPassword);
        message.setTo(to);
        message.setSubject("Your OTP for Login");
        message.setText("Your OTP is: " + otp);
        mailSender.send(message);
    }
}

