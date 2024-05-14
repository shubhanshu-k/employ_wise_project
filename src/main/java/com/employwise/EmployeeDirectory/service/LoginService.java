package com.employwise.EmployeeDirectory.service;

public interface LoginService {
    String generateOTP();
    void sendOTP(String email, String otp);

    void sendPassResetLink(String email, String link);

    boolean verifyOTP(String email, String enteredOTP);

    boolean isAccountLocked(String email);

    boolean isAccountFreezed(String email);




    void incrementResendCount(String email);

    int getResendCount(String email);

    void incrementIncorrectPasswordAttempts(String email);

    int getIncrementAttempts(String email);

    void updatePassword(String email, String password);
}
