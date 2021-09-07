package com.stuartbeard.iorek.notify.event;

import com.stuartbeard.iorek.notify.service.CompromisedPasswordNotificationService;
import com.stuartbeard.iorek.service.PasswordCheckingService;
import com.stuartbeard.iorek.service.model.PasswordCheckResult;
import com.stuartbeard.iorek.service.model.PasswordRiskLevel;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@MockitoSettings
class CompromisedPasswordNotifierTest {

    private static final String REQUEST_FLOW = "login";
    private static final String CREDENTIAL = "credential";
    private static final String PRINCIPAL = "principal";

    @Mock
    private CompromisedPasswordNotificationService compromisedPasswordNotificationService;

    @Mock
    private PasswordCheckingService passwordCheckingService;

    private CompromisedPasswordNotifier compromisedPasswordNotifier;

    private final PasswordCheckResult passwordCheckResult = new PasswordCheckResult()
        .setRiskLevel(PasswordRiskLevel.SEVERELY_COMPROMISED)
        .setCompromisedCount(999999);

    @Test
    void shouldNotCallCompromisedPasswordNotificationServiceWhenInMonitoringMode() {
        setupCompromisedPasswordNotifier(true);

        when(passwordCheckingService.checkPasswordForKnownCompromise(REQUEST_FLOW, CREDENTIAL, false))
            .thenReturn(passwordCheckResult);
        Authentication authentication = mock(UsernamePasswordAuthenticationToken.class);
        when(authentication.getCredentials()).thenReturn(CREDENTIAL);
        AuthenticationSuccessEvent authenticationSuccessEvent = new AuthenticationSuccessEvent(authentication);

        compromisedPasswordNotifier.handleAuthenticationSuccessEvent(authenticationSuccessEvent);

        verify(passwordCheckingService).checkPasswordForKnownCompromise(REQUEST_FLOW, CREDENTIAL, false);
        verify(compromisedPasswordNotificationService, never()).sendNotification(PRINCIPAL, passwordCheckResult);
    }

    @Test
    void shouldCallCompromisedPasswordNotificationServiceWhenUsernamePasswordUsed() {
        setupCompromisedPasswordNotifier(false);

        when(passwordCheckingService.checkPasswordForKnownCompromise(REQUEST_FLOW, CREDENTIAL, false))
            .thenReturn(passwordCheckResult);
        Authentication authentication = mock(UsernamePasswordAuthenticationToken.class);
        when(authentication.getPrincipal()).thenReturn(PRINCIPAL);
        when(authentication.getCredentials()).thenReturn(CREDENTIAL);
        AuthenticationSuccessEvent authenticationSuccessEvent = new AuthenticationSuccessEvent(authentication);

        compromisedPasswordNotifier.handleAuthenticationSuccessEvent(authenticationSuccessEvent);

        verify(passwordCheckingService).checkPasswordForKnownCompromise(REQUEST_FLOW, CREDENTIAL, false);
        verify(compromisedPasswordNotificationService).sendNotification(PRINCIPAL, passwordCheckResult);
    }

    @Test
    void shouldDoNothingWhenNoCredentialsAvailable() {
        setupCompromisedPasswordNotifier(false);

        Authentication authentication = mock(UsernamePasswordAuthenticationToken.class);
        AuthenticationSuccessEvent authenticationSuccessEvent = new AuthenticationSuccessEvent(authentication);

        compromisedPasswordNotifier.handleAuthenticationSuccessEvent(authenticationSuccessEvent);

        verify(passwordCheckingService, never()).checkPasswordForKnownCompromise(REQUEST_FLOW, CREDENTIAL, false);
        verify(compromisedPasswordNotificationService, never()).sendNotification(PRINCIPAL, passwordCheckResult);
    }

    @Test
    void shouldDoNothingWhenNotAUsernamePassword() {
        setupCompromisedPasswordNotifier(false);

        Authentication authentication = mock(AnonymousAuthenticationToken.class);
        AuthenticationSuccessEvent authenticationSuccessEvent = new AuthenticationSuccessEvent(authentication);

        compromisedPasswordNotifier.handleAuthenticationSuccessEvent(authenticationSuccessEvent);

        verify(passwordCheckingService, never()).checkPasswordForKnownCompromise(REQUEST_FLOW, CREDENTIAL, false);
        verify(compromisedPasswordNotificationService, never()).sendNotification(PRINCIPAL, passwordCheckResult);
    }

    private void setupCompromisedPasswordNotifier(boolean monitoringOnly) {
        compromisedPasswordNotifier = new CompromisedPasswordNotifier(passwordCheckingService, compromisedPasswordNotificationService, monitoringOnly);
    }
}
