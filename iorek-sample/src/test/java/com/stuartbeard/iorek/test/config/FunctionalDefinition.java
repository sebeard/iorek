package com.stuartbeard.iorek.test.config;

import io.cucumber.java.After;
import io.cucumber.spring.CucumberContextConfiguration;
import com.stuartbeard.iorek.sample.SampleApplication;
import com.stuartbeard.iorek.test.context.ScenarioContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@CucumberContextConfiguration
@ActiveProfiles(FUNCTIONAL)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = SampleApplication.class
)
@ContextConfiguration(classes = {
    ActionConfiguration.class,
    JacksonAutoConfiguration.class,
})
public class FunctionalDefinition {

    @Autowired
    private ScenarioContext scenarioContext;

    @After
    public void tearDown() {
        scenarioContext.clear();
    }
}
