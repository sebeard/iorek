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
import com.stuartbeard.iorek.service.recorder.PasswordCheckRecorder;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nonnull;

/**
 * Core centralised logic for calculating the number of times a given password has appeared in known credential breaches
 * alongside a perceived password risk level. The password risk level is calculated based on user configuration of the
 * library at runtime and is based on the raw count of appearances identified.
 *
 * @author Stuart Beard
 * @version 1.0.0
 * @since 1.0.0
 */
@RequiredArgsConstructor
@AllArgsConstructor
public class PasswordCheckingService {

    private final CompromisedPasswordService compromisedPasswordService;
    private final CompromisedPasswordThresholdConfigurationProperties credentialSafetyConfig;
    private PasswordCheckRecorder passwordCheckRecorder = new PasswordCheckRecorder() {
    };

    /**
     * Performs a check of the password against an external or customised compromised password service and constructs
     * a PasswordCheckResult object. Records the result into a {@link PasswordCheckRecorder} implementation for analysis.
     *
     * @param requestFlow Which user flow the compromised password check was performed as part of.
     * @param cred A plaintext password or the hash representation of a given password
     * @param isHashRepresentation if the cred parameter is a Hash Representation (true) or Plaintext password (false).
     * @return A constructed password check result containing the raw appearance count and the perceived risk level
     * associated with it.
     */
    public PasswordCheckResult checkPasswordForKnownCompromise(@Nonnull String requestFlow, @Nonnull String cred, boolean isHashRepresentation) {
        int timesCompromised = compromisedPasswordService.getAppearanceCount(cred, isHashRepresentation);

        PasswordCheckResult passwordCheckResult = new PasswordCheckResult()
            .setRiskLevel(getPasswordRiskLevel(timesCompromised))
            .setCompromisedCount(timesCompromised);

        passwordCheckRecorder.recordPasswordCheck(requestFlow, passwordCheckResult);
        return passwordCheckResult;
    }

    /**
     * Calculates the perceived password risk of a given password based on the number of times it has appeared in known
     * credential breaches. Uses configuration to perform calculation such that;
     *
     * <ol>
     * <li>
     *     If appearances in known credential breaches are great than or equal to the configured <strong>severe</strong>
     *     threshold then{@link PasswordRiskLevel.SEVERELY_COMPROMISED} is returned
     * </li>
     * <li>
     *     If appearances in known credential breaches are great than or equal to the configured <strong>warning</strong>
     *     threshold then {@link PasswordRiskLevel.COMPROMISED} is returned
     * </li>
     * <li>
     *     In all other cases {@link PasswordRiskLevel.OK} is returned
     * </li>
     *
     * </ol>
     *
     * @param timesCompromised raw count of the number of times a given password has appeared in known credential
     *                         breaches
     * @return a simple enumeration representing the perceived password risk.
     */
    private PasswordRiskLevel getPasswordRiskLevel(int timesCompromised) {

        if (timesCompromised >= credentialSafetyConfig.getSevere()) {
            return PasswordRiskLevel.SEVERELY_COMPROMISED;
        }

        if (timesCompromised >= credentialSafetyConfig.getWarning()) {
            return PasswordRiskLevel.COMPROMISED;
        }

        return PasswordRiskLevel.OK;
    }
}
