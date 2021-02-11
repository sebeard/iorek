package com.stuartbeard.iorek.function.breach;

import com.stuartbeard.iorek.external.hibp.config.HIBPConfiguration;
import com.stuartbeard.iorek.service.BreachCheckService;
import com.stuartbeard.iorek.service.BreachService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(HIBPConfiguration.class)
public class FunctionConfiguration {

    @Bean
    BreachCheckService breachCheckService(BreachService breachService) {
        return new BreachCheckService(breachService);
    }
}
