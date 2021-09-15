/*
 * MIT License
 *
 * Copyright (c) 2019-2021 Stuart Beard
 */
package com.stuartbeard.iorek.notify.config;

import com.stuartbeard.iorek.notify.event.CompromisedPasswordNotifier;
import com.stuartbeard.iorek.notify.service.CompromisedPasswordNotificationService;
import com.stuartbeard.iorek.pwned.passwords.config.PwnedPasswordsConfiguration;
import com.stuartbeard.iorek.service.PasswordCheckingService;
import com.stuartbeard.iorek.service.config.CommonCompromisedPasswordConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
@Import({
    CommonCompromisedPasswordConfiguration.class,
    PwnedPasswordsConfiguration.class
})
public class CompromisedPasswordNotificationConfiguration {

    /**
     * Provides an EventListener bean for Spring's {@link org.springframework.security.authentication.event.AuthenticationSuccessEvent}
     *
     * @param passwordCheckingService The centralised logic responsible for orchestrating the request to the external
     *                                compromised password service
     * @param notificationService     a notification mechanism that defines a single method for implementer to notify
     *                                consumers about a compromised password out of band without impacting the user journey
     * @param monitoringMode          Provides a means to only monitor for compromised passwords to build up statistics and
     *                                insights before turning on any notification mechanism. Allows implementors to show the
     *                                Business Value Proposition.
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
        return new CompromisedPasswordNotificationService() {
        };
    }
}
