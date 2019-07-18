package com.stuartbeard.iorek.integration.actions;

import com.stuartbeard.iorek.integration.ScenarioContext;
import com.stuartbeard.iorek.integration.model.CredentialSafetyObject;
import com.stuartbeard.iorek.integration.model.IdentityInformationObject;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class IorekActions {

    private final IorekApiActions iorekApiActions;
    private final ScenarioContext scenarioContext;

    public void checkIorekService() {
        assertThat(iorekApiActions.ping(), is(true));
    }

    public void getCredentialSafety(String password, boolean sha1Hashed) {
        CredentialSafetyObject credentialSafetyObject = iorekApiActions.getCredentialSafety(password, sha1Hashed);
        scenarioContext.setContext("credentialSafety", credentialSafetyObject);
    }

    public void getBreachInformation(String email) {
        IdentityInformationObject identityInformationObject = iorekApiActions.getIdentityInfo(email);
        scenarioContext.setContext("breachInformation", identityInformationObject);
    }

    public void verifyPasswordSafety(boolean safe) {
        CredentialSafetyObject credentialSafetyObject = scenarioContext.getContext("credentialSafety");
        assertThat("Password safety mismatch", credentialSafetyObject.isSafe(), is(safe));
    }

    public void verifyPasswordAllowed(boolean allowed) {
        CredentialSafetyObject credentialSafetyObject = scenarioContext.getContext("credentialSafety");
        assertThat("Password allowance mismatch", credentialSafetyObject.isPasswordAllowed(), is(allowed));
    }

    public void verifyPasswordBreachCount(int count) {
        CredentialSafetyObject credentialSafetyObject = scenarioContext.getContext("credentialSafety");
        assertThat("Appearances in Data Set didn't match expected", credentialSafetyObject.getAppearancesInDataSet(), is(count));
    }

    public void verifyDataBreachCount(int count) {
        IdentityInformationObject identityInformationObject = scenarioContext.getContext("breachInformation");
        assertThat("Appearances in Data Breaches didn't match expected", identityInformationObject.getBreaches(), hasSize(count));
    }

    public void verifyPasteCount(int count) {
        IdentityInformationObject identityInformationObject = scenarioContext.getContext("breachInformation");
        assertThat("Appearances in Data Breaches didn't match expected", identityInformationObject.getPastes(), hasSize(count));
    }
}
