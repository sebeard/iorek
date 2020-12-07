package com.stuartbeard.iorek.integration.test.actions;

import com.stuartbeard.iorek.integration.test.context.ContextKey;
import com.stuartbeard.iorek.integration.test.context.ScenarioContext;
import com.stuartbeard.iorek.integration.test.model.CredentialSafetyObject;
import com.stuartbeard.iorek.integration.test.model.IdentityInformationObject;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.assertj.core.api.Assertions.assertThat;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class IorekActions {

    private final IorekApiActions iorekApiActions;
    private final ScenarioContext scenarioContext;

    public void checkIorekService() {
        assertThat(iorekApiActions.ping()).isTrue();
    }

    public void getCredentialSafety(String password, boolean sha1Hashed) {
        CredentialSafetyObject credentialSafetyObject = iorekApiActions.getCredentialSafety(password, sha1Hashed);
        scenarioContext.setContext(ContextKey.CREDENTIAL_SAFETY, credentialSafetyObject);
    }

    public void getBreachInformation(String email) {
        IdentityInformationObject identityInformationObject = iorekApiActions.getIdentityInfo(email);
        scenarioContext.setContext(ContextKey.BREACH_INFORMATION, identityInformationObject);
    }

    public void verifyPasswordSafety(boolean safe) {
        CredentialSafetyObject credentialSafetyObject = scenarioContext.getContext(ContextKey.CREDENTIAL_SAFETY);
        assertThat(credentialSafetyObject.isSafe())
            .withFailMessage("Password safety mismatch")
            .isEqualTo(safe);
    }

    public void verifyPasswordAllowed(boolean allowed) {
        CredentialSafetyObject credentialSafetyObject = scenarioContext.getContext(ContextKey.CREDENTIAL_SAFETY);
        assertThat(credentialSafetyObject.isPasswordAllowed())
            .withFailMessage("Password allowance mismatch")
            .isEqualTo(allowed);
    }

    public void verifyPasswordBreachCount(int count) {
        CredentialSafetyObject credentialSafetyObject = scenarioContext.getContext(ContextKey.CREDENTIAL_SAFETY);
        assertThat(credentialSafetyObject.getAppearancesInDataSet())
            .withFailMessage("Appearances in Data Set didn't match expected")
            .isEqualTo(count);
    }

    public void verifyDataBreachCount(int count) {
        IdentityInformationObject identityInformationObject = scenarioContext.getContext(ContextKey.BREACH_INFORMATION);
        assertThat(identityInformationObject.getBreaches())
            .withFailMessage("Appearances in Data Breaches didn't match expected")
            .hasSize(count);
    }

    public void verifyPasteCount(int count) {
        IdentityInformationObject identityInformationObject = scenarioContext.getContext(ContextKey.BREACH_INFORMATION);
        assertThat(identityInformationObject.getPastes())
            .withFailMessage("Appearances in Data Breaches didn't match expected")
            .hasSize(count);
    }
}
