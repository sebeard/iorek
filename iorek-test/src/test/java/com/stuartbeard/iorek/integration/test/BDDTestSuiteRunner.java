package com.stuartbeard.iorek.integration.test;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resource/features",
    plugin = {"pretty", "html:target/cucumber"},
    glue = {"com.stuartbeard.iorek.integration.test.steps"})
class BDDTestSuiteRunner {

    /**
     * Need this method so the cucumber will recognize this class as glue and load spring context configuration
     */
    @BeforeEach
    void setUp() {
        System.out.println("-------------- Spring Context Initialized For Executing Cucumber Tests --------------");
    }

//    @Autowired
//    WireMock wireMock;

//    @Before
//    public void setup() {
//        setupWiremock();
//    }
//
//    @After
//    public void tearDown() {
//        WireMock.reset();
//    }

    void setupWiremock() {
    }
}
