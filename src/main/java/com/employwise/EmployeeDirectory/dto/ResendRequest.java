package com.employwise.EmployeeDirectory.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ResendRequest {

    @NotBlank(message = "email is required.")
    @Email(regexp = ".+@.+\\..+", message = "email is not a valid email address")
    @JsonProperty("email")
    private String email;

}
