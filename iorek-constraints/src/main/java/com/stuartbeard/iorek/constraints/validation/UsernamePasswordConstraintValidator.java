/*
 * MIT License
 *
 * Copyright (c) 2019-2021 Stuart Beard
 */
package com.stuartbeard.iorek.constraints.validation;


import com.stuartbeard.iorek.constraints.PasswordDoesNotContainUsername;
import lombok.SneakyThrows;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Defines the logic to validate that a given {@link String} password input does not contain the {@link String} username input
 * {@link PasswordDoesNotContainUsername}.
 *
 * @author Stuart Beard
 * @version 1.0.0
 * @since 1.0.0
 */
public class UsernamePasswordConstraintValidator implements ConstraintValidator<PasswordDoesNotContainUsername, Object> {

    private static final String OBJECT_VALUE_KEY = "objectValue";
    private static final String FIELD_VALUE_KEY_FORMAT = "#objectValue.get%s()";

    private static final String DEFAULT_MESSAGE = "{com.stuartbeard.iorek.constraints.PasswordDoesNotContainUsername.message}";
    private static final String DEFAULT_PASSWORD_FIELD = "password";
    private static final String DEFAULT_USERNAME_FIELD = "username";


    private String message = DEFAULT_MESSAGE;
    private String passwordField = DEFAULT_PASSWORD_FIELD;
    private String usernameField = DEFAULT_USERNAME_FIELD;

    /**
     * Initializes the validator in preparation for {@link #isValid(Object, ConstraintValidatorContext)} calls by
     * overriding validator class defaults with those supplied in the annotation namely;
     * * The message returned by the constraint
     * * The field the username is stored in
     * * The field the password is stored in
     *
     * @param constraintAnnotation annotation instance for a given constraint declaration
     */
    @Override
    public void initialize(PasswordDoesNotContainUsername constraintAnnotation) {
        message = constraintAnnotation.message();
        usernameField = constraintAnnotation.usernameField();
        passwordField = constraintAnnotation.passwordField();
    }

    /**
     * Implements the validation logic for checking that a password does not contain the chosen username. Inspects the
     * given object input for the field names specified in {@link PasswordDoesNotContainUsername} extracts their values
     * and ensures the username field value is not contained within the password field value
     *
     * @param valueToValidate            the plain text password input to validate
     * @param constraintValidatorContext context in which the constraint is evaluated
     * @return {@code true} if the password DOES NOT contain the username; {@code false} otherwise
     */
    @SneakyThrows
    @Override
    public boolean isValid(Object valueToValidate, ConstraintValidatorContext constraintValidatorContext) {
        StandardEvaluationContext context = buildEvaluationContext(valueToValidate);
        String username = getFieldValue(usernameField, context);
        String password = getFieldValue(passwordField, context);

        if (password.contains(username)) {
            addConstraintValidation(constraintValidatorContext, message);
            return false;
        }

        return true;
    }

    /**
     * Adds the desired message template (or placeholder) as a constraint violation which can be picked up via
     * exceptions that can be transformed into API responses for the calling client to digest.
     *
     * @param context context in which the constraint is evaluated
     * @param message the message, template, or placeholder to add as a constraint violation.
     */
    private void addConstraintValidation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
            .addConstraintViolation();
    }

    private String getFieldValue(String fieldName, StandardEvaluationContext context) {
        ExpressionParser parser = new SpelExpressionParser();
        String expression = String.format(FIELD_VALUE_KEY_FORMAT, StringUtils.capitalize(fieldName));
        return String.valueOf(parser.parseExpression(expression).getValue(context));
    }

    private StandardEvaluationContext buildEvaluationContext(Object objectValue) {
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariable(OBJECT_VALUE_KEY, objectValue);
        return context;
    }
}
