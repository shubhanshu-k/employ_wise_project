package com.employwise.EmployeeDirectory.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidOTPValidator implements ConstraintValidator<ValidOTP, String> {

    @Override
    public void initialize(ValidOTP constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && value.length() == 6;
    }
}

