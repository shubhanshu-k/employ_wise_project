package com.employwise.EmployeeDirectory.service;

import com.employwise.EmployeeDirectory.model.Employee;
import com.employwise.EmployeeDirectory.repository.EmployeeRepository;
import com.employwise.EmployeeDirectory.dto.EmployeeRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Slf4j
@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public String addEmployee(EmployeeRequest employeeRequest) {
        try {
            Employee employee = mapRequestToEmployee(employeeRequest);
            String employeeId = UUID.randomUUID().toString();
            employee.setId(employeeId);
            log.info("Employee added with ID: {}", employeeId);
            employeeRepository.save(employee);
            return employeeId;
        } catch (Exception e) {
            log.error("failed to add an employee");
            throw new RuntimeException("Failed to add employee");
        }
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
                existingEmployee.setReportsTo(updatedEmployee.getReportsTo());
                existingEmployee.setProfileImage(updatedEmployee.getProfileImage());
                log.info("Employee with id :"+ id+" updated successfully");
                employeeRepository.save(existingEmployee);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log.error("failed to update the details of employee with ID:"+id);
            throw new RuntimeException("Failed to update employee with ID: " + id);
        }
    }

    public Optional<Employee> getEmployeeById(String id) {
        try {
            return employeeRepository.findById(id);
        } catch (Exception e) {
           log.error("Failed to retrieve employee with ID:"+ id);
            throw new RuntimeException("Failed to retrieve employee with ID: " + id);
        }
    }
    private Employee mapRequestToEmployee(EmployeeRequest employeeRequest) {
        Employee employee = new Employee();
        employee.setEmployeeName(employeeRequest.getEmployeeName());
        employee.setPhoneNumber(employeeRequest.getPhoneNumber());
        employee.setEmail(employeeRequest.getEmail());
        employee.setReportsTo(employeeRequest.getReportsTo());
        employee.setProfileImage(employeeRequest.getProfileImage());
        return employee;
    }
}
