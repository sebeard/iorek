/*
 * MIT License
 *
 * Copyright (c) 2019-2021 Stuart Beard
 */
package com.stuartbeard.iorek.notify.event;

import com.stuartbeard.iorek.notify.service.CompromisedPasswordNotificationService;
import com.stuartbeard.iorek.service.PasswordCheckingService;
import com.stuartbeard.iorek.service.model.PasswordCheckResult;
import com.stuartbeard.iorek.service.model.PasswordRiskLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.CredentialsContainer;

/**
 * Class that listens for {@link AuthenticationSuccessEvent} and performs a post authentication password check against
 * an external compromised password checking service before notifying downstream implementations of a detected password
 * compromise.
 *
 * @author Stuart Beard
 * @version 1.0.0
 * @since 1.0.0
 */
@RequiredArgsConstructor
public final class CompromisedPasswordNotifier {

    private static final String REQUEST_FLOW = "authentication";

    private final PasswordCheckingService passwordCheckingService;
    private final CompromisedPasswordNotificationService notificationService;
    private final boolean monitoringOnly;

    /**
     * Asynchronous Event Listener Implementation for handling {@link AuthenticationSuccessEvent}. Performs a number of actions as part
     * of the process flow.
     * First it extracts the password input that successfully authenticated the user
     * (based solely on the {@link UsernamePasswordAuthenticationToken} authentication object).It then uses that
     * password to perform a check against the external password checking service via the centralised logic.
     * Having checked the password for compromise and retrieved the result the handler either ;
     *  - returns without doing anything further - monitoring only mode
     *  - if the password is anything other than {@link PasswordRiskLevel#OK} triggers a notification using the principal and includes the result of the check
     *
     * <p>
     * Finally the credential is erased to ensure it is not discoverable in the code elsewhere.
     *
     * @param event A successful Authentication Event containg details of who or whom successfully authenticated.
     */
    @Async
    @EventListener
    public void handleAuthenticationSuccessEvent(AuthenticationSuccessEvent event) {
        Authentication auth = event.getAuthentication();
        try {
            if (auth instanceof UsernamePasswordAuthenticationToken && auth.getCredentials() != null) {
                PasswordCheckResult passwordCheckResult = passwordCheckingService.checkPasswordForKnownCompromise(REQUEST_FLOW, (String) auth.getCredentials(), false);
                ((UsernamePasswordAuthenticationToken) auth).eraseCredentials();

                if (monitoringOnly) {
                    return;
                }

                // Notify downstream
                if (passwordCheckResult.getRiskLevel() != PasswordRiskLevel.OK) {
                    notificationService.sendNotification(auth.getPrincipal(), passwordCheckResult);
                }
            }
        } finally {
            // As we explicitly need to disable credential erasure in order to check the password correctly we should
            // erase it ourselves after the fact - no matter if we checked the credential or not.
            if (auth instanceof CredentialsContainer) {
                ((CredentialsContainer) auth).eraseCredentials();
            }
        }
    }
}
