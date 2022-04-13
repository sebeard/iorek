/*
 * MIT License
 *
 * Copyright (c) 2019-2021 Stuart Beard
 */
package com.stuartbeard.iorek.constraints;

import com.stuartbeard.iorek.constraints.validation.UsernamePasswordConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation interface that determines which field level elements in a given POJO class should be checked for known
 * password compromise. Applies to 'in-band' notification of password compromise only.
 *
 * @author Stuart Beard
 * @version 1.0.0
 * @since 1.0.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UsernamePasswordConstraintValidator.class})
public @interface PasswordDoesNotContainUsername {

    String message() default "{com.stuartbeard.iorek.constraints.PasswordDoesNotContainUsername.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String usernameField() default "username";

    String passwordField() default "password";

}
