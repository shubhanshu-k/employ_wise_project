package com.employwise.EmployeeDirectory.dto;

import com.employwise.EmployeeDirectory.validators.ValidOTP;
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
public class AuthRequest {

    @NotBlank(message = "email is required.")
    @Email(regexp = ".+@.+\\..+", message = "email is not a valid email address")
    @JsonProperty("email")
    private String email;

    @NotBlank(message = "OTP is required.")
    @ValidOTP
    @JsonProperty("otp")
    private String otp;

}
