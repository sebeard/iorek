package com.stuartbeard.iorek.test;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:features",
    plugin = {"pretty", "com.stuartbeard.iorek.test.functional.InitializationHook"},
    tags = "@functional",
    glue = {"com.stuartbeard.iorek.test.functional"})
public class FunctionalTest {}
