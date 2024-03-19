package com.employwise.EmployeeDirectory.service;

import com.employwise.EmployeeDirectory.dto.EmployeeRequest;
import com.employwise.EmployeeDirectory.model.Employee;
import com.employwise.EmployeeDirectory.repository.EmployeeRepository;
import com.employwise.EmployeeDirectory.utils.ResponseHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class EmployeeService implements EmployeeServiceImplement {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public ResponseEntity<Object> addNewEmployee(EmployeeRequest employeeRequest) {
        ObjectMapper mapper = new ObjectMapper();
        Employee employee = mapper.convertValue(employeeRequest, Employee.class);
        Employee saveEmployee = employeeRepository.save(employee);
        Map<String, Object> successResponse = new HashMap<>();
        successResponse.put("status", "success");
        successResponse.put("saveEmployeeData", saveEmployee);

        return ResponseHandler.responseBuilder("SUCCESS", HttpStatus.OK, successResponse);
    }

    public List<Employee> getAllEmployees() {
        try {
            return employeeRepository.findAll();
        } catch (Exception e) {
            log.error("failed to fetch employees");
            throw new RuntimeException("Failed to retrieve employees");
        }
    }

    public boolean deleteEmployeeById(String id) {
        try {
            if (employeeRepository.existsById(id)) {
                employeeRepository.deleteById(id);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log.error("failed to delete an employee");
            throw new RuntimeException("Failed to delete employee with ID: " + id);
        }
    }

    public boolean updateEmployeeById(String id, EmployeeRequest updatedEmployeeRequest) {
        try {
            Optional<Employee> existingEmployeeOptional = employeeRepository.findById(id);
            if (existingEmployeeOptional.isPresent()) {
                Employee existingEmployee = existingEmployeeOptional.get();
                Employee updatedEmployee = mapRequestToEmployee(updatedEmployeeRequest);
                // Update existing employee fields with data from updatedEmployee
                existingEmployee.setEmployeeName(updatedEmployee.getEmployeeName());
                existingEmployee.setPhoneNumber(updatedEmployee.getPhoneNumber());
                existingEmployee.setEmail(updatedEmployee.getEmail());
                existingEmployee.setPassword(updatedEmployee.getPassword()); // Set password field
                existingEmployee.setReportsTo(updatedEmployee.getReportsTo());
                existingEmployee.setProfileImage(updatedEmployee.getProfileImage());
                log.info("Employee with id :" + id + " updated successfully");
                employeeRepository.save(existingEmployee);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log.error("failed to update the details of employee with ID:" + id);
            throw new RuntimeException("Failed to update employee with ID: " + id);
        }
    }

    public Optional<Employee> getEmployeeById(String id) {
        try {
            return employeeRepository.findById(id);
        } catch (Exception e) {
            log.error("Failed to retrieve employee with ID:" + id);
            throw new RuntimeException("Failed to retrieve employee with ID: " + id);
        }
    }

    private Employee mapRequestToEmployee(EmployeeRequest employeeRequest) {
        Employee employee = new Employee();
        employee.setEmployeeName(employeeRequest.getEmployeeName());
        employee.setPhoneNumber(employeeRequest.getPhoneNumber());
        employee.setEmail(employeeRequest.getEmail());
        employee.setPassword(employeeRequest.getPassword()); // Set password field
        employee.setReportsTo(employeeRequest.getReportsTo());
        employee.setProfileImage(employeeRequest.getProfileImage());
        return employee;
    }

    public void addEmployeesFromCSV(List<EmployeeRequest> employeeRequests) {
        try {
            for (EmployeeRequest employeeRequest : employeeRequests) {
                addNewEmployee(employeeRequest);
            }
            log.info("Employees added from CSV");
        } catch (Exception e) {
            log.error("Failed to add employees from CSV", e);
            throw new RuntimeException("Failed to add employees from CSV", e);
        }
    }

    public Optional<Employee> getEmployeeByEmail(String email) {
        try {
            return employeeRepository.findByEmail(email);
        } catch (Exception e) {
            log.error("Failed to retrieve employee with email:" + email);
            throw new RuntimeException("Failed to retrieve employee with email: " + email);
        }
    }
}
