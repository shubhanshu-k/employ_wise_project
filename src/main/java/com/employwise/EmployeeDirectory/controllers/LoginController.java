package com.employwise.EmployeeDirectory.controllers;


import com.employwise.EmployeeDirectory.dto.AuthRequest;
import com.employwise.EmployeeDirectory.dto.LoginRequest;
import com.employwise.EmployeeDirectory.model.Employee;
import com.employwise.EmployeeDirectory.service.EmailService;
import com.employwise.EmployeeDirectory.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Slf4j
@RestController
public class LoginController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private EmployeeService employeeService;

    // Map to store email and OTP pair
    private Map<String, String> emailOTPMap = new HashMap<>();

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        try {
            // Check if both email and password are present
            if (loginRequest.getEmail() == null || loginRequest.getPassword() == null) {
                log.info("Login failed: Both email and password are required");
                return new ResponseEntity<>("Login failed: Both email and password are required", HttpStatus.BAD_REQUEST);
            }

            // Check if email exists in the database
            Optional<Employee> employeeOptional = employeeService.getEmployeeByEmail(loginRequest.getEmail());
            if (employeeOptional.isEmpty()) {
                log.info("Login failed: Email not registered");
                return new ResponseEntity<>("Login failed: Email not registered", HttpStatus.UNAUTHORIZED);
            }
log.info("you are here 1st");
            // Generate and send OTP
            String OTP = generateOTP();
            log.info("you are here 2nd");
            log.info(" OTP is :" + OTP);
            String emailTemp=loginRequest.getEmail();
            emailService.sendOTP(emailTemp, OTP);
            log.info("you are here 3rd");
            // Store the OTP in the map
            emailOTPMap.put(emailTemp, OTP);
            log.info("you are here 4th");

            return new ResponseEntity<>("OTP sent to your email", HttpStatus.OK);

        } catch (Exception e) {
            log.error("Failed to process login request", e);
            return new ResponseEntity<>("Failed to process login request", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Method to generate a 6-digit OTP
    private String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);

        return String.valueOf(otp);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOTP(@RequestBody AuthRequest authRequest) {
        try {
            String email = authRequest.getEmail();
            String enteredOTP = authRequest.getOtp();

            // Check if OTP is correct
            if (emailOTPMap.containsKey(email) && emailOTPMap.get(email).equals(enteredOTP)) {
                // Clear the OTP from the map after successful login
                emailOTPMap.remove(email);
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
