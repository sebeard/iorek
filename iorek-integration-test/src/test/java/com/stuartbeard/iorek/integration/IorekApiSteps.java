package com.stuartbeard.iorek.integration;

import cucumber.api.java.After;
import cucumber.api.java8.En;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.Assert.fail;

@ContextConfiguration(classes = IntegrationTestConfiguration.class)
public class IorekApiSteps implements En {

    @Autowired
    private ScenarioContext scenarioContext;

    @Autowired
    private IorekActions iorekActions;

    public IorekApiSteps() {

        Given("^the Iorek API Service is running$", () -> {
            iorekActions.checkIorekService();
        });

        When("^I check the safety of my password; (.*)$", (String password) -> {
            iorekActions.getCredentialSafety(password);
        });

        Then("^I expect to be informed that my password is (unsafe|safe)$", (String safe) -> {
            if ("unsafe".equals(safe)) {
                iorekActions.verifyPasswordSafety(false);
                return;
            }

            if ("safe".equals(safe)) {
                iorekActions.verifyPasswordSafety(true);
                return;
            }

            fail(safe + " is not a valid option. Try either 'safe' or 'unsafe'");
        });

        Then("^I expect to be informed that my password is (disallowed|allowed)$", (String allowed) -> {
            if ("disallowed".equals(allowed)) {
                iorekActions.verifyPasswordAllowed(false);
                return;
            }

            if ("allowed".equals(allowed)) {
                iorekActions.verifyPasswordAllowed(true);
                return;
            }

            fail(allowed + " is not a valid option. Try either 'allowed' or 'disallowed'");
        });

        Then("^I expect to be informed that my password has been breached (\\d+) times$", (Integer breachCount) -> {
            iorekActions.verifyPasswordBreachCount(breachCount);
        });
    }

    @After(order = 1)
    public void resetContext() {
        scenarioContext.clear();
    }
}
