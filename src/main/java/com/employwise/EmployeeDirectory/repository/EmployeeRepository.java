package com.employwise.EmployeeDirectory.repository;

import com.employwise.EmployeeDirectory.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Repository
public class EmployeeRepository {
    @Autowired
    private MongoTemplate mongoTemplate;
    private String collectionName = "employee_details";

    public Employee save(Employee employeeDetails) {
        return mongoTemplate.insert(employeeDetails);
    }

    public List<Employee> findAll() {
        return mongoTemplate.findAll(Employee.class, collectionName);
    }

    public boolean existsById(String id) {
        return mongoTemplate.exists(query(where("id").is(id)), Employee.class, collectionName);
    }

    public void deleteById(String id) {
        mongoTemplate.remove(query(where("id").is(id)), Employee.class, collectionName);
    }
    public void updateAccountStatusByEmail(String email, String status) {
        Query query = new Query(Criteria.where("email").is(email));
        Update update = new Update().set("accountStatus", status);
        mongoTemplate.updateFirst(query, update, Employee.class);
    }

    public void updateAccountPasswordByEmail(String email, String password) {
        Query query = new Query(Criteria.where("email").is(email));
        Update update = new Update().set("password", password);
        mongoTemplate.updateFirst(query, update, Employee.class);
    }




    public Optional<Employee> findById(String id) {
        return Optional.ofNullable(mongoTemplate.findOne(query(where("id").is(id)), Employee.class, collectionName));
    }

    public Optional<Employee> findByEmail(String email) {
        return Optional.ofNullable(mongoTemplate.findOne(query(where("email").is(email)), Employee.class, collectionName));
    }
}
