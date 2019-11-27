package com.stuartbeard.iorek.integration.steps;

import com.stuartbeard.iorek.integration.ScenarioContext;
import com.stuartbeard.iorek.integration.actions.IorekActions;
import com.stuartbeard.iorek.integration.config.IntegrationTestConfiguration;
import io.cucumber.java8.En;
import org.junit.After;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static org.apache.commons.codec.digest.DigestUtils.sha1Hex;
import static org.junit.Assert.fail;

@ContextConfiguration(classes = IntegrationTestConfiguration.class)
public class IorekApiSteps implements En {

    @Autowired
    private ScenarioContext scenarioContext;

    @Autowired
    private IorekActions iorekActions;

    public IorekApiSteps() {

        Given("^the Iorek API Service is running$",
            () -> iorekActions.checkIorekService());

        When("^I check the safety of my password; (.*)$",
            (String password) -> iorekActions.getCredentialSafety(password, false));

        When("^I check the safety of my hashed password; (.*)$", (String password) -> {
            String sha1Hash = sha1Hex(password);
            iorekActions.getCredentialSafety(sha1Hash, true);
        });

        When("^I check the breach information for my email address; (.*)$",
            (String email) -> iorekActions.getBreachInformation(email));

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

        Then("^I expect to be informed that my password has been breached (\\d+) times$",
            (Integer breachCount) -> iorekActions.verifyPasswordBreachCount(breachCount));

        Then("^I expect that my email was in (\\d+) data breaches$",
            (Integer breachCount) -> iorekActions.verifyDataBreachCount(breachCount));

        Then("^I expect that my email was in (\\d+) pastes$",
            (Integer pasteCount) -> iorekActions.verifyPasteCount(pasteCount));
    }

    @After
    public void resetContext() {
        scenarioContext.clear();
    }
}
