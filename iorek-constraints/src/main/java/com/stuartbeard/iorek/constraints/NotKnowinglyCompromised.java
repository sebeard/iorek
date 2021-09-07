/*
 * MIT License
 *
 * Copyright (c) 2019-2021 Stuart Beard
 */
package com.stuartbeard.iorek.constraints;

import com.stuartbeard.iorek.constraints.validation.CompromisedPasswordConstraintValidator;
import com.stuartbeard.iorek.service.model.PasswordRiskLevel;

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
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {CompromisedPasswordConstraintValidator.class})
public @interface NotKnowinglyCompromised {

    String message() default "{com.stuartbeard.iorek.constraints.NotKnowinglyCompromised.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     *  @return the request flow defined by the specific API Request Model class if set. The default
     *  is 'unspecified'.
     */
    String requestFlow() default "unspecified";

    /**
     * Provides the maximum level of acceptable risk for a given API Request Object. Setting this will have the
     * following consequences;
     * <br />
     * <ul>
     *     <li>
     *         {@link PasswordRiskLevel#OK} - Allow all passwords that have been compromised less than the warning
     *         threshold defined in
     *         {@link com.stuartbeard.iorek.service.config.CompromisedPasswordThresholdConfigurationProperties}
     *     </li>
     *     <li>
     *         {@link PasswordRiskLevel#COMPROMISED} - Allow all passwords that have been compromised less than the severe
     *         threshold defined in
     *         {@link com.stuartbeard.iorek.service.config.CompromisedPasswordThresholdConfigurationProperties}
     *     </li>
     *     <li>
     *         {@link PasswordRiskLevel#SEVERELY_COMPROMISED} - Allow all passwords to be used (...what's the point
     *         in using this library then?)
     *         {@link com.stuartbeard.iorek.service.config.CompromisedPasswordThresholdConfigurationProperties}
     *     </li>
     * </ul>
     *
     * @return the level of <strong>acceptable</strong> risk request flow defined by the specific API Request Model
     * class if set. The default is {@link PasswordRiskLevel#COMPROMISED} so that by default there is a perceived
     * balance between preventing poor password choices, while keeping it reasonably easy to choose a more secure one.
     * This is impacted by the chosen configuration defined by
     * {@link com.stuartbeard.iorek.service.config.CompromisedPasswordThresholdConfigurationProperties}
     */
    PasswordRiskLevel maxAcceptableRisk() default PasswordRiskLevel.COMPROMISED;

}
