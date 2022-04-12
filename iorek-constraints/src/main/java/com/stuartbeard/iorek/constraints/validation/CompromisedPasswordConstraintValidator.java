/*
 * MIT License
 *
 * Copyright (c) 2019-2021 Stuart Beard
 */
package com.stuartbeard.iorek.constraints.validation;

import com.stuartbeard.iorek.constraints.config.CompromisedPasswordMonitoringProperties;
import com.stuartbeard.iorek.service.PasswordCheckingService;
import com.stuartbeard.iorek.service.model.PasswordCheckResult;
import com.stuartbeard.iorek.service.model.PasswordRiskLevel;
import com.stuartbeard.iorek.constraints.NotKnowinglyCompromised;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Defines the logic to validate that a given {@link String} password input has not been knowingly compromised
 * {@link NotKnowinglyCompromised}. i.e. has not appeared/ has appeared in known data breaches an acceptable amount of
 * times.
 *
 * @author Stuart Beard
 * @version 1.0.0
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class CompromisedPasswordConstraintValidator implements ConstraintValidator<NotKnowinglyCompromised, String> {

    private static final String DEFAULT_MESSAGE = "{com.stuartbeard.iorek.constraints.NotCompromised.message}";
    private static final String DEFAULT_REQUEST_FLOW = "Unknown";

    private final PasswordCheckingService passwordCheckingService;
    private final CompromisedPasswordMonitoringProperties monitoringProperties;

    private String message = DEFAULT_MESSAGE;
    private String requestFlow = DEFAULT_REQUEST_FLOW;
    private PasswordRiskLevel maxAcceptableRiskLevel = PasswordRiskLevel.COMPROMISED;

    /**
     * Initializes the validator in preparation for {@link #isValid(String, ConstraintValidatorContext)} calls by
     * overriding validator class defaults with those supplied in the annotation namely;
     * * The message returned by the constraint
     * * The request flow the password is being used on
     * * The maximum acceptable risk level allowed for sa given flow.
     *
     * @param constraintAnnotation annotation instance for a given constraint declaration
     */
    @Override
    public void initialize(NotKnowinglyCompromised constraintAnnotation) {
        message = constraintAnnotation.message();
        requestFlow = constraintAnnotation.requestFlow();
        maxAcceptableRiskLevel = constraintAnnotation.maxAcceptableRisk();
    }

    /**
     * Implements the validation logic for checking that a password has not been knowingly compromised. Calls out to
     * the password checking service to calculate an associated risk level for for a given password input and uses
     * returned risk level to check if the risk is within an acceptable risk band.
     *
     * @param plainTextToCheck           the plain text password input to validate
     * @param constraintValidatorContext context in which the constraint is evaluated
     * @return {@code true} if this constraint validator has been set to monitoring only mode, or if the risk level
     * calculated on a password input is less than or equivalent to the maximum acceptable risk level; {@code false}
     * otherwise
     */
    @Override
    public boolean isValid(String plainTextToCheck, ConstraintValidatorContext constraintValidatorContext) {
        PasswordCheckResult passwordCheckResult = passwordCheckingService.checkPasswordForKnownCompromise(requestFlow, plainTextToCheck, false);

        // If the intent is to just capture the input and monitor password risk levels (based on configuration) without
        // impacting the UX of the client then after checking the password for known compromise we can simply just
        // return as valid.
        if (monitoringProperties.isOnly()) {
            return true;
        }

        // Use the ordinals of the PasswordCheckLevel enumeration to determine if a given password should be allowed
        // based on configuration.
        if (passwordCheckResult.getRiskLevel().ordinal() <= maxAcceptableRiskLevel.ordinal()) {
            return true;
        }

        addConstraintValidation(constraintValidatorContext, message);
        return false;
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
}
