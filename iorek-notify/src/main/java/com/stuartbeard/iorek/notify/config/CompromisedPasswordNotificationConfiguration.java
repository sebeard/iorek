/*
 * MIT License
 *
 * Copyright (c) 2019-2021 Stuart Beard
 */
package com.stuartbeard.iorek.notify.config;

import com.stuartbeard.iorek.notify.event.CompromisedPasswordNotifier;
import com.stuartbeard.iorek.notify.service.CompromisedPasswordNotificationService;
import com.stuartbeard.iorek.service.PasswordCheckingService;
import com.stuartbeard.iorek.service.config.CompromisedPasswordThresholdConfigurationProperties;
import com.stuartbeard.iorek.service.external.CompromisedPasswordService;
import com.stuartbeard.iorek.service.recorder.PasswordCheckRecorder;
import com.stuartbeard.iorek.pwned.passwords.config.PwnedPasswordsConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Spring Configuration class that sets up Spring Beans for Compromised Password Notifications which perform 'out of band'
 * checks on inbound API requests (i.e. Login or Change Email).
 *
 * @author Stuart Beard
 * @version 1.0.0
 * @since 1.0.0
 */
@Configuration
@Import(PwnedPasswordsConfiguration.class)
public class CompromisedPasswordNotificationConfiguration {

    /**
     * Provides an EventListener bean for Spring's {@link org.springframework.security.authentication.event.AuthenticationSuccessEvent}
     *
     * @param passwordCheckingService The centralised logic responsible for orchestrating the request to the external
     *                                compromised password service
     * @param notificationService a notification mechanism that defines a single method for implementer to notify
     *                            consumers about a compromised password out of band without impacting the user journey
     * @param monitoringMode Provides a means to only monitor for compromised passwords to build up statistics and
     *                       insights before turning on any notification mechanism. Allows implementors to show the
     *                       Business Value Proposition.
     * @return an EventListener bean that monitors successful authentications, and uses the input (successful) password to check
     * for known password compromise. Reports metrics and notifies based on the implementations provided.
     */
    @Bean
    public CompromisedPasswordNotifier compromisedPasswordNotifier(PasswordCheckingService passwordCheckingService,
                                                                   CompromisedPasswordNotificationService notificationService,
                                                                   @Value("${compromised.password.monitoring.only:true}") boolean monitoringMode) {
        return new CompromisedPasswordNotifier(passwordCheckingService, notificationService, monitoringMode);
    }

    /**
     * Conditionally instantiates a Password Checking Service Spring Bean if one has not already been created. This is
     * the centralised logic responsible for orchestrating the request to the external compromised password
     * service, aggregating the result, and calculating the perceived password risk level from configuration.
     *
     * @param compromisedPasswordService An implementation that connects to an external datasource for checking
     *                                   passwords for <strong>known</strong> compromise and returns the count
     *                                   associated with their appearances in known data breaches.
     * @param credentialSafetyConfig     Configuration properties that set the thresholds for when a given password is marked as
     *                                   <code>PasswordRiskLevel.COMPROMISED</code> or
     *                                   <code>PasswordRiskLevel.SEVERELY_COMPROMISED</code>.
     * @param passwordCheckRecorder      Used to record the request flow and password check result
     *                                   (with no identifiable information) against some form of monitoring system.
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
     * Provides a default implementation of the {@link CompromisedPasswordNotificationService} class (i.e. when called
     * to send a notification an {@link UnsupportedOperationException} will be thrown. Consumers of this library should
     * implement their own class that caters to how they want to notify users out of band (e.g. Email,
     * Push Notification, Message with in Application)
     *
     * @return a default instance of the {@link CompromisedPasswordNotificationService} class
     */
    @Bean
    @ConditionalOnMissingBean
    public CompromisedPasswordNotificationService compromisedPasswordNotificationService() {
        return new CompromisedPasswordNotificationService() {};
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
