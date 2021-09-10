/*
 * MIT License
 *
 * Copyright (c) 2019-2021 Stuart Beard
 */
package com.stuartbeard.iorek.constraints.config;

import com.stuartbeard.iorek.service.PasswordCheckingService;
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
@Import(PwnedPasswordsConfiguration.class)
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

    /**
     * Conditionally instantiates a Password Checking Service Spring Bean if one has not already been created. This is
     * the centralised logic responsible for orchestrating the request to the external compromised password
     * service, aggregating the result, and calculating the perceived password risk level from configuration.
     *
     * @param compromisedPasswordService An implementation that connects to an external datasource for checking
     *                                   passwords for <strong>known</strong> compromise and returns the count
     *                                   associated with their appearances in known data breaches.
     * @param credentialSafetyConfig Configuration properties that set the thresholds for when a given password is marked as
     *                               {@code PasswordRiskLevel.COMPROMISED} or
     *                               {@code PasswordRiskLevel.SEVERELY_COMPROMISED}.
     * @param passwordCheckRecorder Used to record the request flow and password check result
     *                              (with no identifiable information) against some form of monitoring system.
     * @return an instance of the core Password Checking Service
     */
    @Bean
    @ConditionalOnMissingBean
    public PasswordCheckingService passwordCheckingService(CompromisedPasswordService compromisedPasswordService,
                                                           CompromisedPasswordThresholdConfigurationProperties credentialSafetyConfig,
                                                           PasswordCheckRecorder passwordCheckRecorder) {
        return new PasswordCheckingService(compromisedPasswordService, credentialSafetyConfig, passwordCheckRecorder);
    }

    /**
     * Conditionally instantiates a Password Check Recorder Spring Bean if one has not already been created. This is to
     * provide a simple zero operation implementation of the interface to ensure that monitoring functionality works correctly.
     *
     * @return a default implementation of the Password Check Recorder interface. This implementation does nothing when called.
     */
    @Bean
    @ConditionalOnMissingBean
    public PasswordCheckRecorder passwordCheckRecorder() {
        return new PasswordCheckRecorder() {};
    }


    @Bean
    @ConditionalOnMissingBean
    @ConfigurationProperties("compromised.password.thresholds")
    public CompromisedPasswordThresholdConfigurationProperties credentialSafetyConfig() {
        return new CompromisedPasswordThresholdConfigurationProperties();
    }
}
