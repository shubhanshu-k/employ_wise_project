package com.employwise.EmployeeDirectory.controllers;

import com.employwise.EmployeeDirectory.dto.EmployeeRequest;
import com.employwise.EmployeeDirectory.model.Employee;
import com.employwise.EmployeeDirectory.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j // Add @Slf4j annotation
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/employees")
    public ResponseEntity<String> addEmployee(@RequestBody EmployeeRequest employeeRequest) {
        try {
            String employeeId = employeeService.addEmployee(employeeRequest);
            log.info("Employee added with ID: {}", employeeId);
            return new ResponseEntity<>(employeeId, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Failed to add employee", e);
            return new ResponseEntity<>("Failed to add employee", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        try {
            List<Employee> employees = employeeService.getAllEmployees();
            log.info("Retrieved all employees");
            return new ResponseEntity<>(employees, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Failed to retrieve employees", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable String id) {
        try {
            boolean success = employeeService.deleteEmployeeById(id);
            if (success) {
                log.info("Employee deleted with ID: {}", id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                log.warn("Employee not found with ID: {}", id);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("Failed to delete employee with ID: " + id, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/employees/{id}")
    public ResponseEntity<Void> updateEmployee(@PathVariable String id, @RequestBody EmployeeRequest updatedEmployeeRequest) {
        try {
            boolean success = employeeService.updateEmployeeById(id, updatedEmployeeRequest);
            if (success) {
                log.info("Employee updated with ID: {}", id);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                log.warn("Employee not found with ID: {}", id);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("Failed to update employee with ID: " + id, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable String id) {
        try {
            Optional<Employee> employeeOptional = employeeService.getEmployeeById(id);
            if (employeeOptional.isPresent()) {
                log.info("Retrieved employee with ID: {}", id);
                return new ResponseEntity<>(employeeOptional.get(), HttpStatus.OK);
            } else {
                log.warn("Employee not found with ID: {}", id);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("Failed to retrieve employee with ID: " + id, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
