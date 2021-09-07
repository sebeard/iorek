/*
 * MIT License
 *
 * Copyright (c) 2019-2021 Stuart Beard
 */
package com.stuartbeard.iorek.service;

import com.stuartbeard.iorek.service.config.CompromisedPasswordThresholdConfigurationProperties;
import com.stuartbeard.iorek.service.external.CompromisedPasswordService;
import com.stuartbeard.iorek.service.model.PasswordCheckResult;
import com.stuartbeard.iorek.service.model.PasswordRiskLevel;
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

    @BeforeEach
    void setupPasswordCheckingService() {
        CompromisedPasswordThresholdConfigurationProperties safetyConfig = new CompromisedPasswordThresholdConfigurationProperties();
        safetyConfig.setWarning(1);
        safetyConfig.setSevere(2);
        passwordCheckingService = new PasswordCheckingService(compromisedPasswordService, safetyConfig);
    }

    @ParameterizedTest
    @MethodSource("inputs")
    void shouldReturnSafeAndAllowedCredentialSafetyWhenNotFoundInDataSet(String input, boolean sha1Hash) {
        when(compromisedPasswordService.getAppearanceCount(input, sha1Hash)).thenReturn(0);

        PasswordCheckResult passwordCheckResult = passwordCheckingService.checkPasswordForKnownCompromise("test", input, sha1Hash);

        verify(compromisedPasswordService).getAppearanceCount(input, sha1Hash);
        assertThat(passwordCheckResult.getCompromisedCount()).isZero();
        assertThat(passwordCheckResult.getRiskLevel()).isEqualTo(PasswordRiskLevel.OK);
    }

    @ParameterizedTest
    @MethodSource("inputs")
    void shouldReturnUnsafeAndButAllowedCredentialSafetyWhenFoundInDataSetWithinWarningThreshold(String input, boolean sha1Hash) {
        when(compromisedPasswordService.getAppearanceCount(input, sha1Hash)).thenReturn(2);

        PasswordCheckResult passwordCheckResult = passwordCheckingService.checkPasswordForKnownCompromise("test", input, sha1Hash);

        verify(compromisedPasswordService).getAppearanceCount(input, sha1Hash);
        assertThat(passwordCheckResult.getCompromisedCount()).isEqualTo(2);
        assertThat(passwordCheckResult.getRiskLevel()).isEqualTo(PasswordRiskLevel.COMPROMISED);
    }

    @ParameterizedTest
    @MethodSource("inputs")
    void shouldReturnUnsafeAndNotAllowedCredentialSafetyWhenFoundInDataSet(String input, boolean sha1Hash) {
        when(compromisedPasswordService.getAppearanceCount(input, sha1Hash)).thenReturn(3);

        PasswordCheckResult passwordCheckResult = passwordCheckingService.checkPasswordForKnownCompromise("test", input, sha1Hash);

        verify(compromisedPasswordService).getAppearanceCount(input, sha1Hash);
        assertThat(passwordCheckResult.getCompromisedCount()).isEqualTo(3);
        assertThat(passwordCheckResult.getRiskLevel()).isEqualTo(PasswordRiskLevel.SEVERELY_COMPROMISED);
    }

    @ParameterizedTest
    @MethodSource("inputs")
    void shouldReturnUnsafeButAllowedCredentialSafetyWhenFoundInDataSetButPreventSevereIsOff(String input, boolean sha1Hash) {
        CompromisedPasswordThresholdConfigurationProperties safetyConfig = new CompromisedPasswordThresholdConfigurationProperties();
        safetyConfig.setWarning(1);
        safetyConfig.setSevere(2);
        passwordCheckingService = new PasswordCheckingService(compromisedPasswordService, safetyConfig);
        when(compromisedPasswordService.getAppearanceCount(input, sha1Hash)).thenReturn(3);

        PasswordCheckResult passwordCheckResult = passwordCheckingService.checkPasswordForKnownCompromise("test", input, sha1Hash);

        verify(compromisedPasswordService).getAppearanceCount(input, sha1Hash);
        assertThat(passwordCheckResult.getCompromisedCount()).isEqualTo(3);
        assertThat(passwordCheckResult.getRiskLevel()).isEqualTo(PasswordRiskLevel.SEVERELY_COMPROMISED);
    }
}
