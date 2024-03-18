package com.employwise.EmployeeDirectory.service;

import com.employwise.EmployeeDirectory.dto.EmployeeRequest;
import org.springframework.http.ResponseEntity;

public interface EmployeeServiceImplement {
    ResponseEntity<Object> addNewEmployee(EmployeeRequest employeeRequest);
}
