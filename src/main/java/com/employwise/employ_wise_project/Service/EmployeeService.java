package com.employwise.employ_wise_project.Service;

import com.employwise.employ_wise_project.model.Employee;
import com.employwise.employ_wise_project.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public String addEmployee(Employee employee) {
        try {
            String employeeId = UUID.randomUUID().toString();
            employee.setId(employeeId);
            employeeRepository.save(employee);
            return employeeId;
        } catch (Exception e) {
            // Handle any exceptions here
            e.printStackTrace(); // or log the exception
            throw new RuntimeException("Failed to add employee");
        }
    }

    public List<Employee> getAllEmployees() {
        try {
            return employeeRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace(); // or log the exception
            throw new RuntimeException("Failed to retrieve employees");
        }
    }

    public Page<Employee> getAllEmployeeswithPaging(Pageable pageable) {
        try {
            return employeeRepository.findAll(pageable);
        } catch (Exception e) {
            e.printStackTrace(); // or log the exception
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
            e.printStackTrace(); // or log the exception
            throw new RuntimeException("Failed to delete employee with ID: " + id);
        }
    }

    public boolean updateEmployeeById(String id, Employee updatedEmployee) {
        try {
            Optional<Employee> existingEmployeeOptional = employeeRepository.findById(id);
            if (existingEmployeeOptional.isPresent()) {
                Employee existingEmployee = existingEmployeeOptional.get();
                existingEmployee.setEmployeeName(updatedEmployee.getEmployeeName());
                existingEmployee.setPhoneNumber(updatedEmployee.getPhoneNumber());
                existingEmployee.setEmail(updatedEmployee.getEmail());
                existingEmployee.setReportsTo(updatedEmployee.getReportsTo());
                existingEmployee.setProfileImage(updatedEmployee.getProfileImage());
                employeeRepository.save(existingEmployee);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace(); // or log the exception
            throw new RuntimeException("Failed to update employee with ID: " + id);
        }
    }

    public Optional<Employee> getEmployeeById(String id) {
        try {
            return employeeRepository.findById(id);
        } catch (Exception e) {
            e.printStackTrace(); // or log the exception
            throw new RuntimeException("Failed to retrieve employee with ID: " + id);
        }
    }
    public Employee getNthLevelManager(String employeeId, int level) {
        try {
            Optional<Employee> employeeOptional = employeeRepository.findById(employeeId);
            if (employeeOptional.isPresent()) {
                Employee employee = employeeOptional.get();
                return findNthLevelManager(employee, level);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace(); // or log the exception
            throw new RuntimeException("Failed to retrieve the nth level manager");
        }
    }

    private Employee findNthLevelManager(Employee employee, int level) {
        if (level == 0) {
            return employee; // Base case: reached the nth level
        } else if (employee.getReportsTo() == null) {
            return null; // Base case: no more managers to traverse
        } else {
            // Recursive call to find the (n-1)th level manager
            return getNthLevelManager(employee.getReportsTo(), level - 1);
        }
    }

}
