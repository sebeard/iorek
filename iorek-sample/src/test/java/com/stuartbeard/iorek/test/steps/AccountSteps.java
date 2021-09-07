/*
 * MIT License
 *
 * Copyright (c) 2019-2021 Stuart Beard
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.stuartbeard.iorek.test.steps;

import com.stuartbeard.iorek.test.actions.AccountActions;
import com.stuartbeard.iorek.test.context.FunctionalTestContextKey;
import com.stuartbeard.iorek.test.context.ScenarioContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@RequiredArgsConstructor
public class AccountSteps {

    private final ScenarioContext scenarioContext;
    private final AccountActions accountActions;

    @Given("^I do not have an existing account$")
    public void noAccount() {
        // NO OP
    }

    @Given("^I have an existing account$")
    public void existingAccount() {
    }

    @Given("^I have an existing account with a (good|bad|terrible) password$")
    public void existingAccountWithPassword(String passwordStrength) {
    }

    @Given("^I have forgotten my password$")
    public void forgottenPassword() {
    }

    @When("^I attempt to register using a known (good|bad|terrible) password$")
    public void registerWithPassword(String passwordStrength) {
    }

    @When("^I attempt to change my password to a known (good|bad|terrible) password$")
    public void changePassword(String passwordStrength) {
    }

    @When("^I attempt to reset my password to a known (good|bad|terrible) password$")
    public void resetPassword(String passwordStrength) {
    }

    @When("^I change my email address$")
    public void changeEmail() {
    }

    @When("^I login$")
    public void login() {
    }

    @Then("^the service (accepts|rejects) my (password reset|password change|registration)$")
    public void serviceResponse(String acceptsRejects, String requestFlow) {
        HttpStatus status = scenarioContext.get(FunctionalTestContextKey.RESPONSE_CODE);
        switch (acceptsRejects) {
            case "accepts":
                assertThat(status.is2xxSuccessful()).isTrue();
                break;
            case "rejects":
                assertThat(status.is4xxClientError()).isTrue();
                break;
            default:
                fail("Unhandled expected status: " + acceptsRejects);
        }
    }

    @Then("^tells me that my password is terrible$")
    public void verifyPasswordIsTerrible() {
        Object errorResponse = scenarioContext.get(FunctionalTestContextKey.ERROR_RESPONSE);
        assertThat(errorResponse).isEqualTo("");
    }

    @Then("^the service notifies out of band me that my password is (bad|terrible)$")
    public void verifyOutOfBandNotification(String passwordStrength) {
        assertThat(scenarioContext.exists(FunctionalTestContextKey.OUT_OF_BAND_NOTIFICATION)).isTrue();
    }

    @Then("^the service does not notify me$")
    public void verifyNoOutOfBandNotification() {
        assertThat(scenarioContext.exists(FunctionalTestContextKey.OUT_OF_BAND_NOTIFICATION)).isFalse();
    }

}
