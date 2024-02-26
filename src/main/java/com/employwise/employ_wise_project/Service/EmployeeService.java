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
            e.printStackTrace();
            throw new RuntimeException("Failed to add employee");
        }
    }

    public List<Employee> getAllEmployees() {
        try {
            return employeeRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
            throw new RuntimeException("Failed to update employee with ID: " + id);
        }
    }

    public Optional<Employee> getEmployeeById(String id) {
        try {
            return employeeRepository.findById(id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve employee with ID: " + id);
        }
    }

}
