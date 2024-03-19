package com.employwise.EmployeeDirectory.controllers;

import com.employwise.EmployeeDirectory.dto.LoginRequest;
import com.employwise.EmployeeDirectory.model.Employee;
import com.employwise.EmployeeDirectory.service.EmployeeService;
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
    private EmployeeService employeeService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        try {
            // Check if email exists in the database
            Optional<Employee> employeeOptional = employeeService.getEmployeeByEmail(loginRequest.getEmail());
            if (employeeOptional.isEmpty()) {
                log.info("Login failed: Email not registered");
                return new ResponseEntity<>("Login failed: Email not registered", HttpStatus.UNAUTHORIZED);
            }

            // Check if the provided password matches the stored password
            Employee employee = employeeOptional.get();
            if (employee.getPassword().equals(loginRequest.getPassword())) {
                log.info("Login successful");
                return new ResponseEntity<>("Login successful", HttpStatus.OK);
            } else {
                log.info("Login failed: Incorrect password");
                return new ResponseEntity<>("Login failed: Incorrect password", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            log.error("Failed to process login request", e);
            return new ResponseEntity<>("Failed to process login request", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
