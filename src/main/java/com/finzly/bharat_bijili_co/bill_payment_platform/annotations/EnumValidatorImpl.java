package com.finzly.bharat_bijili_co.bill_payment_platform.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnumValidatorImpl implements ConstraintValidator<EnumValidator,String> {
    private EnumValidator annotation;

    @Override
    public void initialize(EnumValidator annotation) {
        this.annotation = annotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Can be null if not required
        }

        Object[] enumValues = annotation.enumClass().getEnumConstants();
        for (Object enumValue : enumValues) {
            if (value.equals(((Enum<?>) enumValue).name().toUpperCase())) {
                return true;
            }
        }
        return false;
    }
}
