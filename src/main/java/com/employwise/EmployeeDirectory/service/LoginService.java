package com.employwise.EmployeeDirectory.service;

import com.employwise.EmployeeDirectory.model.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
@Slf4j
@Service
public class LoginService implements LoginServiceImplement {
    private Map<String, String> emailOTPMap = new HashMap<>();

    @Autowired
    private EmailService emailService;

    @Autowired
    private EmployeeService employeeService;

    @Override
    public String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    @Override
    public void sendOTP(String email, String otp) {
        emailService.sendOTP(email, otp);
        emailOTPMap.put(email, otp);
    }

    @Override
    public boolean verifyOTP(String email, String enteredOTP) {
        Optional<Employee> employeeOptional = employeeService.getEmployeeByEmail(email);
        if (employeeOptional.isPresent()) {
            String storedOTP = emailOTPMap.get(email);
            if (storedOTP != null && storedOTP.equals(enteredOTP)) {
                emailOTPMap.remove(email);
                return true;
            }
        }
        return false;
    }
}
