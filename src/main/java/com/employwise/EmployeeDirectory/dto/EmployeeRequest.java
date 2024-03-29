package com.employwise.EmployeeDirectory.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EmployeeRequest {

    @NotBlank(message = "name is required.")
    @JsonProperty("employeeName")
    private String employeeName;

    @NotBlank(message = "phone_Number is required.")
    @Pattern(regexp = "\\d{10}", message = "mobile_number is not a mobile number")
    @JsonProperty("phoneNumber")
    private String phoneNumber;

    @NotBlank(message = "email is required.")
    @Email(regexp = ".+@.+\\..+", message = "email is not a valid email address")
    @JsonProperty("email")
    private String email;

    @NotBlank(message = "password is required.") // New field for password
    @JsonProperty("password") // New field for password
    private String password; // New field for password

    @JsonProperty("reportsTo")
    private String reportsTo;

    @JsonProperty("profileImage")
    private String profileImage;
}
