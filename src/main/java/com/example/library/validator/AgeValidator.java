package com.example.library.validator;

import com.example.library.annotation.ValidAge;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class AgeValidator implements ConstraintValidator<ValidAge, LocalDate> {
    @Override
    public boolean isValid(LocalDate birthDate, ConstraintValidatorContext context) {
        if (birthDate == null) return true;

        LocalDate minDate = LocalDate.now().minusYears(5);
        LocalDate maxDate = LocalDate.now().minusYears(120);

        return birthDate.isBefore(minDate) && birthDate.isAfter(maxDate);
    }
}
