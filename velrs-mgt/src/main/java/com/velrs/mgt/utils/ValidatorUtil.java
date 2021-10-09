package com.velrs.mgt.utils;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Objects;
import java.util.Set;

@Slf4j
public class ValidatorUtil {

    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    private ValidatorUtil() {
    }

    public static void validate(final Object object) {
        if (Objects.isNull(object)) {
            throw new RuntimeException("param is null");
        }
        Set<ConstraintViolation<Object>> constraintViolations = VALIDATOR.validate(object);
        if (Objects.isNull(constraintViolations) || constraintViolations.isEmpty()) {
            return;
        }
        boolean hasError = false;
        StringBuilder stringBuilder = new StringBuilder();
        for (ConstraintViolation<Object> constraintViolation : constraintViolations) {
            if (!constraintViolation.getMessage().isEmpty()) {
                hasError = true;
                stringBuilder.append(constraintViolation.getMessage()).append(",");
                break;
            }
        }
        if (hasError) {
            String message = stringBuilder.toString();
            throw new RuntimeException(message.endsWith(",") ? message.substring(0, message.length() - 1) : message);
        }
    }


}
