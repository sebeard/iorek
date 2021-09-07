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
package com.stuartbeard.iorek.sample.config;

import com.stuartbeard.iorek.notify.config.CompromisedPasswordNotificationConfiguration;
import com.stuartbeard.iorek.notify.service.CompromisedPasswordNotificationService;
import com.stuartbeard.iorek.service.config.CompromisedPasswordThresholdConfigurationProperties;
import com.stuartbeard.iorek.service.model.PasswordCheckResult;
import com.stuartbeard.iorek.service.recorder.PasswordCheckRecorder;
import com.stuartbeard.iorek.config.CompromisedPasswordConstraintConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({
    CompromisedPasswordConstraintConfiguration.class,
    CompromisedPasswordNotificationConfiguration.class
})
@Configuration
@Slf4j
public class ApplicationConfiguration {

    @Bean
    public PasswordCheckRecorder passwordCheckRecorder() {
        return new PasswordCheckRecorder() {
            @Override
            public void recordPasswordCheck(String requestFlow, PasswordCheckResult passwordCheckResult) {

                switch (passwordCheckResult.getRiskLevel()) {

                    case SEVERELY_COMPROMISED:
                        log.warn("Password provided in {} was severely compromised ({} appearances in known data breaches)", requestFlow, passwordCheckResult.getCompromisedCount());
                        break;

                    case COMPROMISED:
                        log.warn("Password provided in {} was compromised ({} appearances in known data breaches)", requestFlow, passwordCheckResult.getCompromisedCount());
                        break;

                    case OK:
                    default:
                        log.info("Password provided in {} was OK ({} appearances in known data breaches)", requestFlow, passwordCheckResult.getCompromisedCount());
                        break;
                }
            }
        };
    }

    @Bean
    public CompromisedPasswordNotificationService compromisedPasswordNotificationService() {
        return new CompromisedPasswordNotificationService() {
            public void sendNotification(Object principal, PasswordCheckResult passwordCheckResult) {
                switch (passwordCheckResult.getRiskLevel()) {

                    case SEVERELY_COMPROMISED:
                        log.warn("Sending notification that password provided was severely compromised ({} appearances in known data breaches)", passwordCheckResult.getCompromisedCount());
                        break;

                    case COMPROMISED:
                        log.warn("Sending notification that password provided was compromised ({} appearances in known data breaches)", passwordCheckResult.getCompromisedCount());
                        break;

                    case OK:
                    default:
                        log.info("Sending notification that password provided was OK ({} appearances in known data breaches)", passwordCheckResult.getCompromisedCount());
                        break;
                }
            }
        };
    }

    @Bean
    @ConfigurationProperties("compromised.password.thresholds")
    public CompromisedPasswordThresholdConfigurationProperties credentialSafetyConfig() {
        return new CompromisedPasswordThresholdConfigurationProperties();
    }

}
