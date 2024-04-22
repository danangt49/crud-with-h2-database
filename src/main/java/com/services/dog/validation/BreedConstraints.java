package com.services.dog.validation;

import com.services.dog.validation.impl.BreedValidatorImpl;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BreedValidatorImpl.class)
public @interface BreedConstraints {
    String message() default "Breed already used";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
