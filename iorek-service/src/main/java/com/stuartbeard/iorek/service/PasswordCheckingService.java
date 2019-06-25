package com.stuartbeard.iorek.service;

import com.stuartbeard.iorek.service.config.CredentialSafetyConfig;
import com.stuartbeard.iorek.service.model.CredentialSafety;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PasswordCheckingService {

    private BreachService breachService;
    private CredentialSafetyConfig credentialSafetyConfig;

    public CredentialSafety checkCredentialSafetyInfo(@Nonnull String password, boolean isSha1Hash) {
        int appearancesInDataSet = breachService.getAppearanceCount(password, isSha1Hash);

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
