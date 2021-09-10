/*
 * MIT License
 *
 * Copyright (c) 2019-2021 Stuart Beard
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.stuartbeard.iorek.service.config;

import com.stuartbeard.iorek.service.PasswordCheckingService;
import com.stuartbeard.iorek.service.external.CompromisedPasswordService;
import com.stuartbeard.iorek.service.recorder.PasswordCheckRecorder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Configuration class that sets up Common Spring Beans for Compromised Password Checks
 *
 * @author Stuart Beard
 * @version 1.0.0
 * @since 1.0.0
 */
@Configuration
public class CommonCompromisedPasswordConfiguration {

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
