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
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@MockitoSettings
class PasswordCheckingServiceTest {

    private static final String PASSWORD = "password";
    private static final String OK = "OK";
    private static final String SEVERE = "SEVERE";
    private static final String WARNING = "WARNING";

    @Mock
    private CompromisedPasswordService compromisedPasswordService;

    private PasswordCheckingService passwordCheckingService;

    private static Stream<Arguments> inputs() {
        return Stream.of(
            Arguments.of(PASSWORD, false),
            Arguments.of(sha1Hex(PASSWORD), true)
        );
    }

    @AfterEach
    void verifyNoSideEffects() {
        verifyNoMoreInteractions(compromisedPasswordService);
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
        passwordCheckingService = new PasswordCheckingService(compromisedPasswordService, safetyConfig);
    }

    @ParameterizedTest
    @MethodSource("inputs")
    void shouldReturnSafeAndAllowedCredentialSafetyWhenNotFoundInDataSet(String input, boolean sha1Hash) {
        when(compromisedPasswordService.getAppearanceCount(input, sha1Hash)).thenReturn(0);

        CredentialSafety credentialSafety = passwordCheckingService.checkCredentialSafetyInfo(input, sha1Hash);

        verify(compromisedPasswordService).getAppearanceCount(input, sha1Hash);
        assertThat(credentialSafety.getAppearancesInDataSet()).isZero();
        assertThat(credentialSafety.getMessage()).isEqualTo(OK);
        assertThat(credentialSafety.isPasswordAllowed()).isTrue();
        assertThat(credentialSafety.isSafe()).isTrue();
    }

    @ParameterizedTest
    @MethodSource("inputs")
    void shouldReturnUnsafeAndButAllowedCredentialSafetyWhenFoundInDataSetWithinWarningThreshold(String input, boolean sha1Hash) {
        when(compromisedPasswordService.getAppearanceCount(input, sha1Hash)).thenReturn(2);

        CredentialSafety credentialSafety = passwordCheckingService.checkCredentialSafetyInfo(input, sha1Hash);

        verify(compromisedPasswordService).getAppearanceCount(input, sha1Hash);
        assertThat(credentialSafety.getAppearancesInDataSet()).isEqualTo(2);
        assertThat(credentialSafety.getMessage()).isEqualTo(WARNING);
        assertThat(credentialSafety.isPasswordAllowed()).isTrue();
        assertThat(credentialSafety.isSafe()).isFalse();
    }

    @ParameterizedTest
    @MethodSource("inputs")
    void shouldReturnUnsafeAndNotAllowedCredentialSafetyWhenFoundInDataSet(String input, boolean sha1Hash) {
        when(compromisedPasswordService.getAppearanceCount(input, sha1Hash)).thenReturn(3);

        CredentialSafety credentialSafety = passwordCheckingService.checkCredentialSafetyInfo(input, sha1Hash);

        verify(compromisedPasswordService).getAppearanceCount(input, sha1Hash);
        assertThat(credentialSafety.getAppearancesInDataSet()).isEqualTo(3);
        assertThat(credentialSafety.getMessage()).isEqualTo(SEVERE);
        assertThat(credentialSafety.isPasswordAllowed()).isFalse();
        assertThat(credentialSafety.isSafe()).isFalse();
    }

    @ParameterizedTest
    @MethodSource("inputs")
    void shouldReturnUnsafeButAllowedCredentialSafetyWhenFoundInDataSetButPreventSevereIsOff(String input, boolean sha1Hash) {
        CredentialSafetyConfig safetyConfig = new CredentialSafetyConfig();
        safetyConfig.setOk(new ThresholdMessage().setMessage(OK));
        safetyConfig.setWarning(new ThresholdMessage().setThreshold(1).setMessage(WARNING));
        safetyConfig.setSevere(new ThresholdMessage().setThreshold(2).setMessage(SEVERE));
        safetyConfig.setPreventSevere(false);
        passwordCheckingService = new PasswordCheckingService(compromisedPasswordService, safetyConfig);
        when(compromisedPasswordService.getAppearanceCount(input, sha1Hash)).thenReturn(3);

        CredentialSafety credentialSafety = passwordCheckingService.checkCredentialSafetyInfo(input, sha1Hash);

        verify(compromisedPasswordService).getAppearanceCount(input, sha1Hash);
        assertThat(credentialSafety.getAppearancesInDataSet()).isEqualTo(3);
        assertThat(credentialSafety.getMessage()).isEqualTo(SEVERE);
        assertThat(credentialSafety.isPasswordAllowed()).isTrue();
        assertThat(credentialSafety.isSafe()).isFalse();
    }
}
