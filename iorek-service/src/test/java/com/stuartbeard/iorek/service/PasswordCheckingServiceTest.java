package com.stuartbeard.iorek.service;

import com.stuartbeard.iorek.service.config.CredentialSafetyConfig;
import com.stuartbeard.iorek.service.config.ThresholdMessage;
import com.stuartbeard.iorek.service.model.CredentialSafety;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import static org.apache.commons.codec.digest.DigestUtils.sha1Hex;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

@MockitoSettings
class PasswordCheckingServiceTest {

    private static final String PASSWORD = "password";

    @Mock
    private BreachService breachService;

    private PasswordCheckingService passwordCheckingService;

    private static ThresholdMessage thresholdMessage(int threshold, String message) {
        ThresholdMessage thresholdMessage = new ThresholdMessage();
        thresholdMessage.setMessage(message);
        thresholdMessage.setThreshold(threshold);
        return thresholdMessage;
    }

    private static String sha1Prefix() {
        return sha1Hex("password");
    }

    @BeforeEach
    void setupPasswordCheckingService() {
        CredentialSafetyConfig safetyConfig = new CredentialSafetyConfig();
        safetyConfig.setOk(new ThresholdMessage());
        safetyConfig.setWarning(new ThresholdMessage());
        safetyConfig.setSevere(new ThresholdMessage());
        safetyConfig.setPreventSevere(true);
        passwordCheckingService = new PasswordCheckingService(breachService, safetyConfig);
    }

    @AfterEach
    void verifyNoSideEffects() {
        verifyNoMoreInteractions(breachService);
    }

    @Test
    void shouldReturnSafeAndAllowedCredentialSafetyWhenNotFoundInDataSet() {
        when(breachService.getAppearanceCount(sha1Prefix())).thenReturn(0);

        CredentialSafety credentialSafety = passwordCheckingService.checkCredentialSafetyInfo(PASSWORD);

        verify(breachService).getAppearanceCount(sha1Prefix());
        assertThat(credentialSafety.getAppearancesInDataSet(), is(0));
        assertThat(credentialSafety.getMessage(), is(""));
        assertThat(credentialSafety.isPasswordAllowed(), is(true));
        assertThat(credentialSafety.isSafe(), is(true));
    }

    @Test
    void shouldReturnUnsafeAndNotAllowedCredentialSafetyWhenFoundInDataSet() {
        when(breachService.getAppearanceCount(sha1Prefix())).thenReturn(1);

        CredentialSafety credentialSafety = passwordCheckingService.checkCredentialSafetyInfo(PASSWORD);

        verify(breachService).getAppearanceCount(sha1Prefix());
        assertThat(credentialSafety.getAppearancesInDataSet(), is(1));
        assertThat(credentialSafety.getMessage(), is(""));
        assertThat(credentialSafety.isPasswordAllowed(), is(false));
        assertThat(credentialSafety.isSafe(), is(false));
    }

    @Test
    void shouldReturnUnsafeButAllowedCredentialSafetyWhenFoundInDataSetButPreventSevereIsOff() {
        CredentialSafetyConfig safetyConfig = new CredentialSafetyConfig();
        safetyConfig.setOk(new ThresholdMessage());
        safetyConfig.setWarning(new ThresholdMessage());
        safetyConfig.setSevere(new ThresholdMessage());
        safetyConfig.setPreventSevere(false);
        passwordCheckingService = new PasswordCheckingService(breachService, safetyConfig);
        when(breachService.getAppearanceCount(sha1Prefix())).thenReturn(0);

        CredentialSafety credentialSafety = passwordCheckingService.checkCredentialSafetyInfo(PASSWORD);

        verify(breachService).getAppearanceCount(sha1Prefix());
        assertThat(credentialSafety.getAppearancesInDataSet(), is(0));
        assertThat(credentialSafety.getMessage(), is(0));
        assertThat(credentialSafety.isPasswordAllowed(), is(true));
        assertThat(credentialSafety.isSafe(), is(true));
    }
}
