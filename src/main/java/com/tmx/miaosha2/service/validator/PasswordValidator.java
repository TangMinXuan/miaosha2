package com.tmx.miaosha2.service.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<PasswordNotEmpty, String> {

    private boolean required = false;

    @Override
    public void initialize(PasswordNotEmpty constraintAnnotation) {
        required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(required) {
            return !value.equals("d88a2192181a4051a7b50312a62bbfa9");
        }
        return true;
    }
}
