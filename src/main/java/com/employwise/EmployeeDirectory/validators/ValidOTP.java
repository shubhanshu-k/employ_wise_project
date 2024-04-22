package com.employwise.EmployeeDirectory.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidOTPValidator.class)
@Target( { ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidOTP {
    String message() default "OTP must be 6 characters long";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
