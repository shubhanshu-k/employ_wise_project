package com.employwise.EmployeeDirectory.controllers;

import com.employwise.EmployeeDirectory.dto.AuthRequest;
import com.employwise.EmployeeDirectory.dto.LoginRequest;
import com.employwise.EmployeeDirectory.model.Employee;
import com.employwise.EmployeeDirectory.service.EmailService;
import com.employwise.EmployeeDirectory.service.EmployeeService;
import com.employwise.EmployeeDirectory.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        try {
            // Check if both email and password are present
            if (loginRequest.getEmail() == null || loginRequest.getPassword() == null) {
                log.info("Login failed: Both email and password are required");
                return new ResponseEntity<>("Login failed: Both email and password are required", HttpStatus.BAD_REQUEST);
            }

            // Check if email exists in the database
            // Assuming EmployeeService is used to check email existence
            Optional<Employee> employeeOptional = employeeService.getEmployeeByEmail(loginRequest.getEmail());
            if (employeeOptional.isEmpty()) {
                log.info("Login failed: Email not registered");
                return new ResponseEntity<>("Login failed: Email not registered", HttpStatus.UNAUTHORIZED);
            }

            // Generate and send OTP
            String OTP = loginService.generateOTP();
            loginService.sendOTP(loginRequest.getEmail(), OTP);

            return new ResponseEntity<>("OTP sent to your email", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Failed to process login request", e);
            return new ResponseEntity<>("Failed to process login request", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOTP(@RequestBody AuthRequest authRequest) {
        try {
            String email = authRequest.getEmail();
            String enteredOTP = authRequest.getOtp();
            Optional<Employee> employeeOptional = employeeService.getEmployeeByEmail(email);
            if (employeeOptional.isEmpty()) {
                log.info("Login failed: Email not registered");
                return new ResponseEntity<>("Login failed: Email not registered", HttpStatus.UNAUTHORIZED);
            }

            // Verify OTP
            if (loginService.verifyOTP(email, enteredOTP)) {
                log.info("Login successful");
                return new ResponseEntity<>("Login successful", HttpStatus.OK);
            } else {
                log.info("Login failed: Incorrect OTP");
                return new ResponseEntity<>("Login failed: Incorrect OTP", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            log.error("Failed to verify OTP", e);
            return new ResponseEntity<>("Failed to verify OTP", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
