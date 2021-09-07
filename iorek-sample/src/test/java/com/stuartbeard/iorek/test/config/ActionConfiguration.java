package com.stuartbeard.iorek.test.config;

import com.stuartbeard.iorek.test.actions.AccountActions;
import com.stuartbeard.iorek.test.context.ScenarioContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ActionConfiguration {

    @Bean
    public AccountActions requestActions(ScenarioContext scenarioContext,
                                         @Qualifier("authorizationServer") TestRestTemplate template) {
        return new AccountActions(scenarioContext, template);
    }

}
