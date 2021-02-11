package com.stuartbeard.iorek.service;

import com.stuartbeard.iorek.service.config.CredentialSafetyConfig;
import com.stuartbeard.iorek.service.model.CredentialSafety;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nonnull;

@RequiredArgsConstructor
public class PasswordCheckingService {

    private final CompromisedPasswordService compromisedPasswordService;
    private final CredentialSafetyConfig credentialSafetyConfig;

    public CredentialSafety checkCredentialSafetyInfo(@Nonnull String password, boolean isSha1Hash) {
        int appearancesInDataSet = compromisedPasswordService.getAppearanceCount(password, isSha1Hash);

        return new CredentialSafety()
            .setSafe(appearancesInDataSet <= credentialSafetyConfig.getOk().getThreshold())
            .setPasswordAllowed(isPasswordAllowed(appearancesInDataSet))
            .setAppearancesInDataSet(appearancesInDataSet)
            .setMessage(getMessage(appearancesInDataSet));
    }

    private String getMessage(int appearancesInDataSet) {
        if (appearancesInDataSet > credentialSafetyConfig.getSevere().getThreshold()) {
            return String.format(credentialSafetyConfig.getSevere().getMessage(), appearancesInDataSet);
        }

        if (appearancesInDataSet > credentialSafetyConfig.getWarning().getThreshold()) {
            return String.format(credentialSafetyConfig.getWarning().getMessage(), appearancesInDataSet);
        }

        return credentialSafetyConfig.getOk().getMessage();
    }

    private boolean isPasswordAllowed(int appearancesInDataSet) {
        if (credentialSafetyConfig.isPreventSevere()) {
            return appearancesInDataSet <= credentialSafetyConfig.getSevere().getThreshold();
        }

        return true;
    }
}
