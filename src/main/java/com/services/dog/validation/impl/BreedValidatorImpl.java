package com.services.dog.validation.impl;

import com.services.dog.entity.Dogs;
import com.services.dog.repository.DogsRepository;
import com.services.dog.validation.BreedConstraints;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class BreedValidatorImpl implements ConstraintValidator<BreedConstraints, String> {
    private final DogsRepository dogsRepository;
    
    @Override
    public void initialize(BreedConstraints constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String breed, ConstraintValidatorContext context) {
        Optional<Dogs> existingDog = dogsRepository.findByBreed(breed);
        return existingDog.isEmpty();
    }
}
