package com.employwise.EmployeeDirectory.service;

public interface LoginService {
    String generateOTP();
    void sendOTP(String email, String otp);
    boolean verifyOTP(String email, String enteredOTP);
}
