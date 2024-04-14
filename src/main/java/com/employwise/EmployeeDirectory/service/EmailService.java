package com.employwise.EmployeeDirectory.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendOTP(String email, String OTP) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("csmypvt@gmail.com");
        message.setTo(email);
        message.setSubject("Your OTP for Login");
        message.setText("Your OTP is: " + OTP);
        javaMailSender.send(message);
        System.out.println("Mail Sent successfully...");
    }
}
