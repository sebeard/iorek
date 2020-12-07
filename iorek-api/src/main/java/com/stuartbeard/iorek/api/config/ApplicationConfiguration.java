package com.stuartbeard.iorek.api.config;

import com.stuartbeard.iorek.external.hibp.config.HIBPConfiguration;
import com.stuartbeard.iorek.external.hibp.config.PwnedPasswordsConfiguration;
import com.stuartbeard.iorek.service.BreachCheckService;
import com.stuartbeard.iorek.service.BreachService;
import com.stuartbeard.iorek.service.CompromisedPasswordService;
import com.stuartbeard.iorek.service.PasswordCheckingService;
import com.stuartbeard.iorek.service.config.CredentialSafetyConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
    HIBPConfiguration.class,
    PwnedPasswordsConfiguration.class
})
public class ApplicationConfiguration {

    @Bean
    BreachCheckService breachCheckService(BreachService breachService) {
        return new BreachCheckService(breachService);
    }

    @Bean
    PasswordCheckingService passwordCheckingService(CompromisedPasswordService compromisedPasswordService,
                                                    CredentialSafetyConfig credentialSafetyConfig) {
        return new PasswordCheckingService(compromisedPasswordService, credentialSafetyConfig);
    }
}
