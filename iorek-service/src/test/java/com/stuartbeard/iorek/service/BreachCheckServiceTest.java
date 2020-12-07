package com.stuartbeard.iorek.service;

import com.stuartbeard.iorek.service.model.BreachInformation;
import com.stuartbeard.iorek.service.model.IdentityInformation;
import com.stuartbeard.iorek.service.model.PasteInformation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.time.LocalDate;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@MockitoSettings
class BreachCheckServiceTest {

    private static final String MESSAGE = "Your email address appeared in %d known data breaches, and %d pastes.";
    private static final String EMAIL = "someone@test.com";
    private static final BreachInformation BREACH = new BreachInformation()
        .setBreachDate(LocalDate.now())
        .setDomain("https://www.example.com")
        .setName("Breach Example")
        .setInformationBreached(singletonList("Passwords"));
    private static final PasteInformation PASTE = new PasteInformation()
        .setTitle("Paste Example")
        .setAdded(LocalDate.now());

    @Mock
    private BreachService breachService;

    @InjectMocks
    private BreachCheckService breachCheckService;

    private static String expectedMessage(int breachSize, int pasteSize) {
        return String.format(MESSAGE, breachSize, pasteSize);
    }

    @AfterEach
    void verifyNoSideEffects() {
        verifyNoMoreInteractions(breachService);
    }

    @Test
    void shouldReturnIdentityInformationWhenBreachesAndPastesFound() {
        when(breachService.getBreachInformation(EMAIL)).thenReturn(singletonList(BREACH));
        when(breachService.getPasteInformation(EMAIL)).thenReturn(singletonList(PASTE));

        IdentityInformation identityInfo = breachCheckService.checkIdentity(EMAIL);

        verify(breachService).getBreachInformation(EMAIL);
        verify(breachService).getPasteInformation(EMAIL);
        //assertThat(identityInfo.getChecked()).isEqualTo(0);
        assertThat(identityInfo.getMessage()).isEqualTo(expectedMessage(1, 1));
        assertThat(identityInfo.getBreaches())
            .hasSize(1)
            .contains(BREACH);
        assertThat(identityInfo.getPastes())
            .hasSize(1)
            .contains(PASTE);
    }

    @Test
    void shouldReturnIdentityInformationWhenOnlyBreachesFound() {
        when(breachService.getBreachInformation(EMAIL)).thenReturn(singletonList(BREACH));
        when(breachService.getPasteInformation(EMAIL)).thenReturn(emptyList());

        IdentityInformation identityInfo = breachCheckService.checkIdentity(EMAIL);

        verify(breachService).getBreachInformation(EMAIL);
        verify(breachService).getPasteInformation(EMAIL);
        //assertThat(identityInfo.getChecked()).isEqualTo(0);
        assertThat(identityInfo.getMessage()).isEqualTo(expectedMessage(1, 0));
        assertThat(identityInfo.getBreaches())
            .hasSize(1)
            .contains(BREACH);
        assertThat(identityInfo.getPastes()).isEmpty();
    }

    @Test
    void shouldReturnIdentityInformationWhenOnlyPastesFound() {
        when(breachService.getBreachInformation(EMAIL)).thenReturn(emptyList());
        when(breachService.getPasteInformation(EMAIL)).thenReturn(singletonList(PASTE));

        IdentityInformation identityInfo = breachCheckService.checkIdentity(EMAIL);

        verify(breachService).getBreachInformation(EMAIL);
        verify(breachService).getPasteInformation(EMAIL);
        //assertThat(identityInfo.getChecked()).isEqualTo(0);
        assertThat(identityInfo.getMessage()).isEqualTo(expectedMessage(0, 1));
        assertThat(identityInfo.getBreaches()).isEmpty();
        assertThat(identityInfo.getPastes())
            .hasSize(1)
            .contains(PASTE);

    }

    @Test
    void shouldReturnIdentityInformationWhenNoBreachesOrPastesFound() {
        when(breachService.getBreachInformation(EMAIL)).thenReturn(emptyList());
        when(breachService.getPasteInformation(EMAIL)).thenReturn(emptyList());

        IdentityInformation identityInfo = breachCheckService.checkIdentity(EMAIL);

        verify(breachService).getBreachInformation(EMAIL);
        verify(breachService).getPasteInformation(EMAIL);
        //assertThat(identityInfo.getChecked()).isEqualTo(0);
        assertThat(identityInfo.getMessage()).isEqualTo(expectedMessage(0, 0));
        assertThat(identityInfo.getBreaches()).isEmpty();
        assertThat(identityInfo.getPastes()).isEmpty();
    }
}
