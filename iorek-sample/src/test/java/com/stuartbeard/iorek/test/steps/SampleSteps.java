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

import com.stuartbeard.iorek.sample.SimpleCompromisedPasswordNotificationService;
import com.stuartbeard.iorek.sample.SimplePasswordCheckRecorder;
import com.stuartbeard.iorek.service.model.PasswordCheckResult;
import com.stuartbeard.iorek.service.model.PasswordRiskLevel;
import com.stuartbeard.iorek.test.actions.SampleActions;
import com.stuartbeard.iorek.test.context.FunctionalTestContextKey;
import com.stuartbeard.iorek.test.context.ScenarioContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@RequiredArgsConstructor
public class SampleSteps {

    private final ScenarioContext scenarioContext;
    private final SampleActions sampleActions;
    private final SimpleCompromisedPasswordNotificationService notificationService;
    private final SimplePasswordCheckRecorder passwordCheckRecorder;

    @When("^I call the service with a known (good|bad|terrible) password in the payload$")
    public void inBandAlert(String passwordStrength) {
        sampleActions.inBandAlert(passwordStrength);
    }

    @When("^I call the service using a known (good|bad|terrible) password as my credential$")
    public void outOfBandAlert(String passwordStrength) {
        sampleActions.outOfBandAlert(passwordStrength);
    }

    @Then("^the service (accepts|rejects) my request$")
    public void serviceResponse(String acceptsRejects) {
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
        String errorResponse = scenarioContext.get(FunctionalTestContextKey.RESPONSE);
        assertThat(errorResponse).isEqualTo("The password provided for in-band-sample was too severely compromised and cannot be used.");
    }

    @Then("^the service notifies out of band me that my password is (bad|terrible)$")
    public void verifyOutOfBandNotification(String passwordStrength) {
        PasswordRiskLevel expectedRiskLevel = null;
        switch (passwordStrength) {
            case "bad":
                expectedRiskLevel = PasswordRiskLevel.COMPROMISED;
                break;
            case "terrible":
                expectedRiskLevel = PasswordRiskLevel.SEVERELY_COMPROMISED;
                break;
            default:
                fail("Unhandled password strength variable");
        }
        String principal = scenarioContext.get(FunctionalTestContextKey.PRINCIPAL);
        PasswordCheckResult passwordCheckResult = notificationService.getPasswordCheckResultByPrincipal().get(principal);
        assertThat(passwordCheckResult)
            .isNotNull()
            .extracting(PasswordCheckResult::getRiskLevel).isEqualTo(expectedRiskLevel);
    }

    @Then("^the service does not notify me$")
    public void verifyNoOutOfBandNotification() {
        assertThat(notificationService.getPasswordCheckResultByPrincipal()).isEmpty();
    }

    @Then("^the password for (authentication|in-band-sample) is recorded as (good|bad|terrible)$")
    public void verifyRecordedResult(String requestFlow, String passwordStrength) {
        PasswordRiskLevel expectedRiskLevel = null;
        switch (passwordStrength) {
            case "good":
                expectedRiskLevel = PasswordRiskLevel.OK;
                break;
            case "bad":
                expectedRiskLevel = PasswordRiskLevel.COMPROMISED;
                break;
            case "terrible":
                expectedRiskLevel = PasswordRiskLevel.SEVERELY_COMPROMISED;
                break;
            default:
                fail("Unhandled password strength variable");
        }

        PasswordCheckResult passwordCheckResult = passwordCheckRecorder.getLatestPasswordCheckResultByRequestFlow().get(requestFlow);
        assertThat(passwordCheckResult)
            .isNotNull()
            .extracting(PasswordCheckResult::getRiskLevel).isEqualTo(expectedRiskLevel);
    }

}
