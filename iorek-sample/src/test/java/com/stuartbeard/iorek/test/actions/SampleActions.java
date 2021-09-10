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
package com.stuartbeard.iorek.test.actions;

import com.stuartbeard.iorek.test.context.FunctionalTestContextKey;
import com.stuartbeard.iorek.test.context.ScenarioContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

@RequiredArgsConstructor
public class SampleActions {

    private static final String GOOD_PASSWORD = "thisIsAnAwesomelySecurePasswordThatHasNotBeenCompromisedKnowinglyYet";
    private static final String BAD_PASSWORD = "oreoCookie1";
    private static final String TERRIBLE_PASSWORD = "password";

    private final SampleApiActions sampleApiActions;
    private final ScenarioContext scenarioContext;

    public void inBandAlert(String passwordStrength) {
        ResponseEntity<String> response;
        switch (passwordStrength) {
            case "bad":
                response = sampleApiActions.inBandRequest(BAD_PASSWORD);
                break;
            case "terrible":
                response = sampleApiActions.inBandRequest(TERRIBLE_PASSWORD);
                break;
            case "good":
            default:
                response = sampleApiActions.inBandRequest(GOOD_PASSWORD);
                break;
        }
        scenarioContext.put(FunctionalTestContextKey.RESPONSE_CODE, response.getStatusCode());
        scenarioContext.put(FunctionalTestContextKey.RESPONSE, response.getBody());
    }

    public void outOfBandAlert(String passwordStrength) {
        ResponseEntity<String> response;
        switch (passwordStrength) {
            case "bad":
                response = sampleApiActions.outOfBandRequest("user2", BAD_PASSWORD);
                scenarioContext.put(FunctionalTestContextKey.PRINCIPAL, "user2");
                break;
            case "terrible":
                response = sampleApiActions.outOfBandRequest("user1", TERRIBLE_PASSWORD);
                scenarioContext.put(FunctionalTestContextKey.PRINCIPAL, "user1");
                break;
            case "good":
            default:
                response = sampleApiActions.outOfBandRequest("user3", GOOD_PASSWORD);
                scenarioContext.put(FunctionalTestContextKey.PRINCIPAL, "user3");
                break;
        }
        scenarioContext.put(FunctionalTestContextKey.RESPONSE_CODE, response.getStatusCode());
        scenarioContext.put(FunctionalTestContextKey.RESPONSE, response.getBody());
    }
}
