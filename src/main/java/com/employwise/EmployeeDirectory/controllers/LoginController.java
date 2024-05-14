package com.employwise.EmployeeDirectory.controllers;

import com.employwise.EmployeeDirectory.dto.AuthRequest;
import com.employwise.EmployeeDirectory.dto.LoginRequest;
import com.employwise.EmployeeDirectory.dto.ResendRequest;
import com.employwise.EmployeeDirectory.model.Employee;
import com.employwise.EmployeeDirectory.service.EmployeeService;
import com.employwise.EmployeeDirectory.service.LoginService;
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

@Slf4j
@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private EmployeeService employeeService;

    private Map<String, Integer> resendCountMap = new HashMap<>();
    private Map<String, Integer> IncorrectPasswordMap = new HashMap<>();

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        try {
            if (loginRequest.getEmail() == null || loginRequest.getPassword() == null) {
                log.info("Login failed: Both email and password are required");
                return new ResponseEntity<>("Login failed: Both email and password are required", HttpStatus.BAD_REQUEST);
            }
            String email = loginRequest.getEmail();

            Optional<Employee> employeeOptional = employeeService.getEmployeeByEmail(email);
            if (employeeOptional.isEmpty()) {
                log.info("Login failed: Email not registered");
                return new ResponseEntity<>("Login failed: Email not registered", HttpStatus.UNAUTHORIZED);
            }
            if (loginService.isAccountLocked(email)) {
                log.info("Login failed: Account is locked");
                return new ResponseEntity<>("Login failed: Account is locked", HttpStatus.UNAUTHORIZED);
            }

            if (loginService.isAccountFreezed(email)) {
                log.info("Login failed: Account is freezed");
                return new ResponseEntity<>("Login failed: Account is freezed", HttpStatus.UNAUTHORIZED);
            }

            if (!employeeService.getPasswordByEmail(email).equals(loginRequest.getPassword())) {
                log.info("Login failed: Incorrect password");

                // Increment incorrect password attempt counter
//                int incorrectPasswordAttempts = loginService.getIncrementAttempts(loginRequest.getEmail());

                int incorrectCount = IncorrectPasswordMap.getOrDefault(email, 0);
                incorrectCount++;
                IncorrectPasswordMap.put(email, incorrectCount);

                // If incorrect password attempts exceed threshold, freeze account
                if (incorrectCount >= 3) {
                    employeeService.updateAccountStatus(email, "freezed");
                    String link = "";
                    loginService.sendPassResetLink(email, link);
                    log.info("Account has been freezed due to multiple incorrect password attempts , please reset your password");
                    return new ResponseEntity<>("Login failed: Account has been freezed due to multiple incorrect password attempts, please reset your pasword", HttpStatus.UNAUTHORIZED);
                }

                return new ResponseEntity<>("Login failed: Incorrect password", HttpStatus.UNAUTHORIZED);
            }


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
            if (email == null || enteredOTP == null) {
                log.info("Login failed: Both email and OTP are required");
                return new ResponseEntity<>("Login failed: Both email and OTP are required", HttpStatus.BAD_REQUEST);
            }

            Optional<Employee> employeeOptional = employeeService.getEmployeeByEmail(email);
            if (employeeOptional.isEmpty()) {
                log.info("Login failed: Email not registered");
                return new ResponseEntity<>("Login failed: Email not registered", HttpStatus.UNAUTHORIZED);
            }

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

    @PostMapping("/resend-otp")
    public ResponseEntity<String> resendOTP(@RequestBody ResendRequest resendRequest) {
        try {
            String email = resendRequest.getEmail();
            if (email == null) {
                log.info("Resend OTP failed: Email is required");
                return new ResponseEntity<>("Resend OTP failed: Email is required", HttpStatus.BAD_REQUEST);
            }

            Optional<Employee> employeeOptional = employeeService.getEmployeeByEmail(email);
            if (employeeOptional.isEmpty()) {
                log.info("Resend OTP failed: Email not registered");
                return new ResponseEntity<>("Resend OTP failed: Email not registered", HttpStatus.UNAUTHORIZED);
            }

            int resendCount = resendCountMap.getOrDefault(email, 0);
            resendCount++;
            resendCountMap.put(email, resendCount);

            if (resendCount > 2) {
                if (!loginService.isAccountLocked(email)) {
                    employeeService.updateAccountStatus(email, "locked");
                }
                log.info(String.valueOf(resendCount));
                log.info("Account locked for email: {}", email);
                return new ResponseEntity<>("Account locked due to multiple OTP resend requests. Please try Again after some time", HttpStatus.UNAUTHORIZED);
            }

            String OTP = loginService.generateOTP();
            loginService.sendOTP(email, OTP);

            return new ResponseEntity<>("New OTP sent to your email", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Failed to resend OTP", e);
            return new ResponseEntity<>("Failed to resend OTP", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody LoginRequest loginRequest) {

        try {
            if (loginRequest.getEmail() == null || loginRequest.getPassword() == null) {
                log.info("Login failed: Both email and password are required");
                return new ResponseEntity<>("Login failed: Both email and password are required", HttpStatus.BAD_REQUEST);
            }
            String email = loginRequest.getEmail();

            Optional<Employee> employeeOptional = employeeService.getEmployeeByEmail(email);
            if (employeeOptional.isEmpty()) {
                log.info("Reset Password  failed: Email not registered");
                return new ResponseEntity<>("Reset Password  failed: Email not registered", HttpStatus.UNAUTHORIZED);
            }

            employeeService.updateAccountStatus(email, "active");
            loginService.updatePassword(email, loginRequest.getPassword());

        } catch (Exception e) {

        }

        return new ResponseEntity<>("your password has been changed successfully. You can now login again", HttpStatus.OK);
    }
}




