package com.employwise.EmployeeDirectory.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.employwise.EmployeeDirectory.model.Employee;

public interface EmployeeRepository extends MongoRepository<Employee, String> {

}

