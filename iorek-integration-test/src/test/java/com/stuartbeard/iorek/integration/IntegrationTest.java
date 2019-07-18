package com.stuartbeard.iorek.integration;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources",
    plugin = {"pretty", "html:target/cucumber"},
    glue = {"com.stuartbeard.iorek.integration.steps"},
    strict = true)
public class IntegrationTest {
}
