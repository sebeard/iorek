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
    private static final String OK = "OK";
    private static final String SEVERE = "SEVERE";
    private static final String WARNING = "WARNING";

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
        safetyConfig.setOk(new ThresholdMessage().setMessage(OK));
        safetyConfig.setWarning(new ThresholdMessage().setThreshold(1).setMessage(WARNING));
        safetyConfig.setSevere(new ThresholdMessage().setThreshold(2).setMessage(SEVERE));
        safetyConfig.setPreventSevere(true);
        passwordCheckingService = new PasswordCheckingService(breachService, safetyConfig);
    }

    @AfterEach
    void verifyNoSideEffects() {
        verifyNoMoreInteractions(breachService);
    }

    @Test
    void shouldReturnSafeAndAllowedCredentialSafetyWhenNotFoundInDataSet() {
        when(breachService.getAppearanceCount(PASSWORD)).thenReturn(0);

        CredentialSafety credentialSafety = passwordCheckingService.checkCredentialSafetyInfo(PASSWORD);

        verify(breachService).getAppearanceCount(PASSWORD);
        assertThat(credentialSafety.getAppearancesInDataSet(), is(0));
        assertThat(credentialSafety.getMessage(), is(OK));
        assertThat(credentialSafety.isPasswordAllowed(), is(true));
        assertThat(credentialSafety.isSafe(), is(true));
    }

    @Test
    void shouldReturnUnsafeAndButAllowedCredentialSafetyWhenFoundInDataSetWithinWarningThreshold() {
        when(breachService.getAppearanceCount(PASSWORD)).thenReturn(2);

        CredentialSafety credentialSafety = passwordCheckingService.checkCredentialSafetyInfo(PASSWORD);

        verify(breachService).getAppearanceCount(PASSWORD);
        assertThat(credentialSafety.getAppearancesInDataSet(), is(2));
        assertThat(credentialSafety.getMessage(), is(WARNING));
        assertThat(credentialSafety.isPasswordAllowed(), is(true));
        assertThat(credentialSafety.isSafe(), is(false));
    }

    @Test
    void shouldReturnUnsafeAndNotAllowedCredentialSafetyWhenFoundInDataSet() {
        when(breachService.getAppearanceCount(PASSWORD)).thenReturn(3);

        CredentialSafety credentialSafety = passwordCheckingService.checkCredentialSafetyInfo(PASSWORD);

        verify(breachService).getAppearanceCount(PASSWORD);
        assertThat(credentialSafety.getAppearancesInDataSet(), is(3));
        assertThat(credentialSafety.getMessage(), is(SEVERE));
        assertThat(credentialSafety.isPasswordAllowed(), is(false));
        assertThat(credentialSafety.isSafe(), is(false));
    }

    @Test
    void shouldReturnUnsafeButAllowedCredentialSafetyWhenFoundInDataSetButPreventSevereIsOff() {
        CredentialSafetyConfig safetyConfig = new CredentialSafetyConfig();
        safetyConfig.setOk(new ThresholdMessage().setMessage(OK));
        safetyConfig.setWarning(new ThresholdMessage().setThreshold(1).setMessage(WARNING));
        safetyConfig.setSevere(new ThresholdMessage().setThreshold(2).setMessage(SEVERE));
        safetyConfig.setPreventSevere(false);
        passwordCheckingService = new PasswordCheckingService(breachService, safetyConfig);
        when(breachService.getAppearanceCount(PASSWORD)).thenReturn(3);

        CredentialSafety credentialSafety = passwordCheckingService.checkCredentialSafetyInfo(PASSWORD);

        verify(breachService).getAppearanceCount(PASSWORD);
        assertThat(credentialSafety.getAppearancesInDataSet(), is(3));
        assertThat(credentialSafety.getMessage(), is(SEVERE));
        assertThat(credentialSafety.isPasswordAllowed(), is(true));
        assertThat(credentialSafety.isSafe(), is(false));
    }
}
