package com.employwise.EmployeeDirectory.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class EmployeeRequest {
    @NotBlank(message = "Employee name is required")
    private String employeeName;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "\\d{10}", message = "Invalid phone number")
    private String phoneNumber;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    private String reportsTo;

    private String profileImage;

    public EmployeeRequest() {
    }

    public EmployeeRequest(String employeeName, String phoneNumber, String email, String reportsTo, String profileImage) {
        this.employeeName = employeeName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.reportsTo = reportsTo;
        this.profileImage = profileImage;
    }
}
