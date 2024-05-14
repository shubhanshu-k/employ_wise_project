package com.employwise.EmployeeDirectory.service.impl;

import com.employwise.EmployeeDirectory.model.Employee;
import com.employwise.EmployeeDirectory.repository.EmployeeRepository;
import com.employwise.EmployeeDirectory.service.EmailService;
import com.employwise.EmployeeDirectory.service.EmployeeService;
import com.employwise.EmployeeDirectory.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
@Slf4j
@Service
public class LoginServiceImplement implements LoginService {
    private Map<String, String> emailOTPMap = new HashMap<>();

    private Map<String, String> emailLinkMap = new HashMap<>();

    private Map<String, Integer> resendCountMap = new HashMap<>();

    private Map<String, Integer> IncorrectPasswordMap = new HashMap<>();

    @Autowired
    private EmailService emailService;
    @Autowired
    private EmployeeRepository employeeRepository;

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
    public void sendPassResetLink(String email, String link) {
        emailService.sendPassResetLink(email, link);
        emailLinkMap.put(email,link);
    }

    @Override
    public boolean verifyOTP(String email, String enteredOTP) {
//        Optional<Employee> employeeOptional = employeeService.getEmployeeByEmail(email);
//        if (employeeOptional.isPresent()) {
            String storedOTP = emailOTPMap.get(email);
            if (storedOTP != null && storedOTP.equals(enteredOTP)) {
                emailOTPMap.remove(email);
                return true;
            }

        return false;
    }

    @Override
    public boolean isAccountLocked(String email) {
        Optional<Employee> employeeOptional = employeeService.getEmployeeByEmail(email);
        return employeeOptional.isPresent() && "locked".equals(employeeOptional.get().getAccountStatus());
    }

    @Override
    public boolean isAccountFreezed(String email) {
        Optional<Employee> employeeOptional = employeeService.getEmployeeByEmail(email);
        return employeeOptional.isPresent() && "freezed".equals(employeeOptional.get().getAccountStatus());
    }


    @Override
    public void incrementResendCount(String email) {
        int resendCount = resendCountMap.getOrDefault(email, 0) + 1;
        resendCountMap.put(email, resendCount);
    }

    @Override
    public int getResendCount(String email) {
        return resendCountMap.getOrDefault(email, 0);
    }

    @Override
    public void incrementIncorrectPasswordAttempts(String email) {
        int incorrectPasswordCount=IncorrectPasswordMap.getOrDefault(email,0)+1;
        IncorrectPasswordMap.put(email,incorrectPasswordCount);
    }

    @Override
    public int getIncrementAttempts(String email) {
        return IncorrectPasswordMap.getOrDefault(email, 0);
    }

    @Override
    public void updatePassword(String email, String password) {
        try {
            Optional<Employee> employeeOptional = employeeRepository.findByEmail(email);
            if (employeeOptional.isPresent()) {
                Employee employee = employeeOptional.get();
                employee.setPassword(password);
//                employeeRepository.save(employee);
                employeeRepository.updateAccountPasswordByEmail(email,password);
                log.info("employeedetails"+employee+" with password"+ employee.getPassword());
            } else {
                // Handle case when employee is not found
                log.warn("Employee with email {} not found", email);
            }
        } catch (Exception e) {
            log.error("Failed to update account password for email: {}", email, e);
        }
    }
}
