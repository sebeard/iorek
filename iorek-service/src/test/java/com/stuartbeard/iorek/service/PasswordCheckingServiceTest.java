package com.stuartbeard.iorek.service;

import com.stuartbeard.iorek.service.config.CredentialSafetyConfig;
import com.stuartbeard.iorek.service.config.ThresholdMessage;
import com.stuartbeard.iorek.service.model.CredentialSafety;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.stream.Stream;

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

    private static Stream<Arguments> inputs() {
        return Stream.of(
            Arguments.of(PASSWORD, false),
            Arguments.of(sha1Hex(PASSWORD), true)
        );
    }

    @AfterEach
    void verifyNoSideEffects() {
        verifyNoMoreInteractions(breachService);
    }

    private static ThresholdMessage thresholdMessage(int threshold, String message) {
        ThresholdMessage thresholdMessage = new ThresholdMessage();
        thresholdMessage.setMessage(message);
        thresholdMessage.setThreshold(threshold);
        return thresholdMessage;
    }

    @BeforeEach
    void setupPasswordCheckingService() {
        CredentialSafetyConfig safetyConfig = new CredentialSafetyConfig();
        safetyConfig.setOk(thresholdMessage(0, OK));
        safetyConfig.setWarning(thresholdMessage(1, WARNING));
        safetyConfig.setSevere(thresholdMessage(2, SEVERE));
        safetyConfig.setPreventSevere(true);
        passwordCheckingService = new PasswordCheckingService(breachService, safetyConfig);
    }

    @ParameterizedTest
    @MethodSource("inputs")
    void shouldReturnSafeAndAllowedCredentialSafetyWhenNotFoundInDataSet(String input, boolean sha1Hash) {
        when(breachService.getAppearanceCount(input, sha1Hash)).thenReturn(0);

        CredentialSafety credentialSafety = passwordCheckingService.checkCredentialSafetyInfo(input, sha1Hash);

        verify(breachService).getAppearanceCount(input, sha1Hash);
        assertThat(credentialSafety.getAppearancesInDataSet(), is(0));
        assertThat(credentialSafety.getMessage(), is(OK));
        assertThat(credentialSafety.isPasswordAllowed(), is(true));
        assertThat(credentialSafety.isSafe(), is(true));
    }

    @ParameterizedTest
    @MethodSource("inputs")
    void shouldReturnUnsafeAndButAllowedCredentialSafetyWhenFoundInDataSetWithinWarningThreshold(String input, boolean sha1Hash) {
        when(breachService.getAppearanceCount(input, sha1Hash)).thenReturn(2);

        CredentialSafety credentialSafety = passwordCheckingService.checkCredentialSafetyInfo(input, sha1Hash);

        verify(breachService).getAppearanceCount(input, sha1Hash);
        assertThat(credentialSafety.getAppearancesInDataSet(), is(2));
        assertThat(credentialSafety.getMessage(), is(WARNING));
        assertThat(credentialSafety.isPasswordAllowed(), is(true));
        assertThat(credentialSafety.isSafe(), is(false));
    }

    @ParameterizedTest
    @MethodSource("inputs")
    void shouldReturnUnsafeAndNotAllowedCredentialSafetyWhenFoundInDataSet(String input, boolean sha1Hash) {
        when(breachService.getAppearanceCount(input, sha1Hash)).thenReturn(3);

        CredentialSafety credentialSafety = passwordCheckingService.checkCredentialSafetyInfo(input, sha1Hash);

        verify(breachService).getAppearanceCount(input, sha1Hash);
        assertThat(credentialSafety.getAppearancesInDataSet(), is(3));
        assertThat(credentialSafety.getMessage(), is(SEVERE));
        assertThat(credentialSafety.isPasswordAllowed(), is(false));
        assertThat(credentialSafety.isSafe(), is(false));
    }

    @ParameterizedTest
    @MethodSource("inputs")
    void shouldReturnUnsafeButAllowedCredentialSafetyWhenFoundInDataSetButPreventSevereIsOff(String input, boolean sha1Hash) {
        CredentialSafetyConfig safetyConfig = new CredentialSafetyConfig();
        safetyConfig.setOk(new ThresholdMessage().setMessage(OK));
        safetyConfig.setWarning(new ThresholdMessage().setThreshold(1).setMessage(WARNING));
        safetyConfig.setSevere(new ThresholdMessage().setThreshold(2).setMessage(SEVERE));
        safetyConfig.setPreventSevere(false);
        passwordCheckingService = new PasswordCheckingService(breachService, safetyConfig);
        when(breachService.getAppearanceCount(input, sha1Hash)).thenReturn(3);

        CredentialSafety credentialSafety = passwordCheckingService.checkCredentialSafetyInfo(input, sha1Hash);

        verify(breachService).getAppearanceCount(input, sha1Hash);
        assertThat(credentialSafety.getAppearancesInDataSet(), is(3));
        assertThat(credentialSafety.getMessage(), is(SEVERE));
        assertThat(credentialSafety.isPasswordAllowed(), is(true));
        assertThat(credentialSafety.isSafe(), is(false));
    }
}
