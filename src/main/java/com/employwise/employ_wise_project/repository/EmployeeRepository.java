package com.employwise.employ_wise_project.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.employwise.employ_wise_project.model.Employee;

public interface EmployeeRepository extends MongoRepository<Employee, String> {

}

