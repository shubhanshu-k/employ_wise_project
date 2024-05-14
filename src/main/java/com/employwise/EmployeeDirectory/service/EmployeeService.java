package com.employwise.EmployeeDirectory.service;

import com.employwise.EmployeeDirectory.dto.EmployeeRequest;
import com.employwise.EmployeeDirectory.model.Employee;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    ResponseEntity<Object> addNewEmployee(EmployeeRequest employeeRequest);
     List<Employee> getAllEmployees();

    boolean deleteEmployeeById(String id);

    boolean updateEmployeeById(String id, EmployeeRequest updatedEmployeeRequest);

    void updateAccountStatus(String email, String status);

    Optional<Employee> getEmployeeById(String id);

    String getPasswordByEmail(String email);

    void addEmployeesFromCSV(List<EmployeeRequest> employeeRequests);

    Optional<Employee> getEmployeeByEmail(String email);


}
