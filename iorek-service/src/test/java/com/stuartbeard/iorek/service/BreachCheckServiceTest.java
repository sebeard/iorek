package com.stuartbeard.iorek.service;

import com.stuartbeard.iorek.service.model.BreachInformation;
import com.stuartbeard.iorek.service.model.IdentityInformation;
import com.stuartbeard.iorek.service.model.PasteInformation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.sql.Date;
import java.time.Instant;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

@MockitoSettings
class BreachCheckServiceTest {

    private static final String MESSAGE = "Your email address appeared in %d known data breaches, and %d pastes.";
    private static final String EMAIL = "someone@test.com";
    private static final BreachInformation BREACH = new BreachInformation()
        .setBreachDate(Date.from(Instant.now()))
        .setDomain("https://www.example.com")
        .setName("Breach Example")
        .setInformationBreached(singletonList("Passwords"));
    private static final PasteInformation PASTE = new PasteInformation()
        .setTitle("Paste Example")
        .setAdded(Date.from(Instant.now()));

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
        //assertThat(identityInfo.getChecked(), is(0));
        assertThat(identityInfo.getMessage(), is(expectedMessage(1, 1)));
        assertThat(identityInfo.getBreaches(), hasSize(1));
        assertThat(identityInfo.getBreaches(), contains(BREACH));
        assertThat(identityInfo.getPastes(), hasSize(1));
        assertThat(identityInfo.getPastes(), contains(PASTE));
    }

    @Test
    void shouldReturnIdentityInformationWhenOnlyBreachesFound() {
        when(breachService.getBreachInformation(EMAIL)).thenReturn(singletonList(BREACH));
        when(breachService.getPasteInformation(EMAIL)).thenReturn(emptyList());

        IdentityInformation identityInfo = breachCheckService.checkIdentity(EMAIL);

        verify(breachService).getBreachInformation(EMAIL);
        verify(breachService).getPasteInformation(EMAIL);
        //assertThat(identityInfo.getChecked(), is(0));
        assertThat(identityInfo.getMessage(), is(expectedMessage(1, 0)));
        assertThat(identityInfo.getBreaches(), hasSize(1));
        assertThat(identityInfo.getBreaches(), contains(BREACH));
        assertThat(identityInfo.getPastes(), hasSize(0));
    }

    @Test
    void shouldReturnIdentityInformationWhenOnlyPastesFound() {
        when(breachService.getBreachInformation(EMAIL)).thenReturn(emptyList());
        when(breachService.getPasteInformation(EMAIL)).thenReturn(singletonList(PASTE));

        IdentityInformation identityInfo = breachCheckService.checkIdentity(EMAIL);

        verify(breachService).getBreachInformation(EMAIL);
        verify(breachService).getPasteInformation(EMAIL);
        //assertThat(identityInfo.getChecked(), is(0));
        assertThat(identityInfo.getMessage(), is(expectedMessage(0, 1)));
        assertThat(identityInfo.getBreaches(), hasSize(0));
        assertThat(identityInfo.getPastes(), hasSize(1));
        assertThat(identityInfo.getPastes(), contains(PASTE));

    }

    @Test
    void shouldReturnIdentityInformationWhenNoBreachesOrPastesFound() {
        when(breachService.getBreachInformation(EMAIL)).thenReturn(emptyList());
        when(breachService.getPasteInformation(EMAIL)).thenReturn(emptyList());

        IdentityInformation identityInfo = breachCheckService.checkIdentity(EMAIL);

        verify(breachService).getBreachInformation(EMAIL);
        verify(breachService).getPasteInformation(EMAIL);
        //assertThat(identityInfo.getChecked(), is(0));
        assertThat(identityInfo.getMessage(), is(expectedMessage(0, 0)));
        assertThat(identityInfo.getBreaches(), hasSize(0));
        assertThat(identityInfo.getPastes(), hasSize(0));
    }
}
