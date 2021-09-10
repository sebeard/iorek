/*
 * MIT License
 *
 * Copyright (c) 2019-2021 Stuart Beard
 */
package com.stuartbeard.iorek.constraints.config;

import com.stuartbeard.iorek.service.PasswordCheckingService;
import com.stuartbeard.iorek.service.config.CommonCompromisedPasswordConfiguration;
import com.stuartbeard.iorek.service.config.CompromisedPasswordThresholdConfigurationProperties;
import com.stuartbeard.iorek.service.external.CompromisedPasswordService;
import com.stuartbeard.iorek.service.recorder.PasswordCheckRecorder;
import com.stuartbeard.iorek.constraints.validation.CompromisedPasswordConstraintValidator;
import com.stuartbeard.iorek.pwned.passwords.config.PwnedPasswordsConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Spring Configuration class that sets up Spring Beans for Compromised Password Constraints which perform 'in-band'
 * checks on inbound API requests (i.e. any field that is annotated with <code>@NotKnowinglyCompromised</code>).
 *
 * @author Stuart Beard
 * @version 1.0.0
 * @since 1.0.0
 *
 * @see PwnedPasswordsConfiguration
 */
@Configuration
@Import({
    CommonCompromisedPasswordConfiguration.class,
    PwnedPasswordsConfiguration.class
})
public class CompromisedPasswordConstraintConfiguration {

    /**
     * Instantiates a Compromised Password constraint validator Spring Bean. This needs to be established as a bean to ensure that
     * the {@link PasswordCheckingService} can be passed in the constructor
     *
     * @param passwordCheckingService The service responsible for orchestrating the request to the external compromised
     *                                password service, aggregating the result, and calculating the perceived password
     *                                risk level from configuration.
     * @param monitoringOnly Instructs the validator to only monitor perceived password risk without causing the request
     *                       to potentially return a constraint violation. Monitoring can be used to demonstrate to
     *                       Service Owners how poor user or client passwords are from a known data breach perspective.
     * @return an instance of the Compromised Password constraint validator
     */
    @Bean
    public CompromisedPasswordConstraintValidator passwordConstraintValidator(PasswordCheckingService passwordCheckingService,
                                                                              @Value("${compromised.password.monitoring.only:true}") boolean monitoringOnly) {
        return new CompromisedPasswordConstraintValidator(passwordCheckingService);
    }
}
