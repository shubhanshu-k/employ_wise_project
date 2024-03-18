package com.employwise.EmployeeDirectory.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "employee_details")
public class Employee {
    @Id
    @Field("employee_id")
    private String id;

    @Field("employeeName")
    private String employeeName;
    @Field("phoneNumber")
    private String phoneNumber;
    @Field("email")
    private String email;
    @Field("reportsTo")
    private String reportsTo;
    @Field("profileImage")
    private String profileImage;

}


