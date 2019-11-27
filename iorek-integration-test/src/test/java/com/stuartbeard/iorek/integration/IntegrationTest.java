package com.stuartbeard.iorek.integration;

import com.github.tomakehurst.wiremock.client.WireMock;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources",
    plugin = {"pretty", "html:target/cucumber"},
    glue = {"com.stuartbeard.iorek.integration.steps"},
    strict = true)
public class IntegrationTest {

    @Autowired
    WireMock wireMock;

    @Before
    public void setup() {
        setupWiremock();
    }

    @After
    public void tearDown() {
        WireMock.reset();
    }

    void setupWiremock() {
    }
}
