package com.employwise.EmployeeDirectory.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EmployeeRequest {
    @NotBlank(message = "name is required.")
    @JsonProperty("employeeName")
    private String employeeName;

    @NotNull(message = "phone_Number is required.")
    @JsonProperty("phoneNumber")
    @Pattern(regexp = "\\d{10}", message = "mobile_number is not a mobile number")
    private String phoneNumber;

    @NotBlank(message = "email is required.")
    @Email(regexp = ".+@.+\\..+", message = "email is not a valid email address")
    @JsonProperty("email")
    private String email;

    private String reportsTo;

    private String profileImage;


//    public EmployeeRequest(String employeeName, String phoneNumber, String email, String reportsTo, String profileImage) {
//        this.employeeName = employeeName;
//        this.phoneNumber = phoneNumber;
//        this.email = email;
//        this.reportsTo = reportsTo;
//        this.profileImage = profileImage;
//    }
}
