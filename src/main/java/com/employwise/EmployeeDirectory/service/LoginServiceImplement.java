package com.employwise.EmployeeDirectory.service;

import com.employwise.EmployeeDirectory.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

public interface LoginServiceImplement {
    String generateOTP();
    void sendOTP(String email, String otp);
    boolean verifyOTP(String email, String enteredOTP);
}
