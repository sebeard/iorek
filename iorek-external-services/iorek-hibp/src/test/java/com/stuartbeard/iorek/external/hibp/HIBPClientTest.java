package com.stuartbeard.iorek.external.hibp;

import com.stuartbeard.iorek.external.hibp.exception.HIBPNotFoundException;
import com.stuartbeard.iorek.external.hibp.exception.HIBPTooManyRequestsException;
import com.stuartbeard.iorek.external.hibp.mapper.HIBPResponseMapper;
import com.stuartbeard.iorek.external.hibp.model.Breach;
import com.stuartbeard.iorek.external.hibp.model.Paste;
import com.stuartbeard.iorek.external.hibp.service.HIBPService;
import com.stuartbeard.iorek.service.model.BreachInformation;
import com.stuartbeard.iorek.service.model.PasteInformation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

import static feign.Util.RETRY_AFTER;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@MockitoSettings
class HIBPClientTest {

    private static final String EMAIL = "someone@test.com";
    private static final String PASTE_TITLE = "PasteTitle";
    private static final String BREACH_NAME = "BreachName";
    private static final String BREACH_TITLE = "BreachTitle";
    private static final String BREACH_DOMAIN = "BreachDomain";
    private static final String DATA_CLASS_1 = "DataClass1";
    private static final String DATA_CLASS_2 = "DataClass2";
    private static final LocalDate BREACH_DATE = LocalDate.now();
    private static final ZonedDateTime ADDED_DATE = ZonedDateTime.now();
    private static final ZonedDateTime MODIFIED_DATE = ZonedDateTime.now();
    private static final LocalDate PASTE_DATE = LocalDate.now();

    @Mock
    private HIBPService hibpService;

    private HIBPClient hibpClient;

    @BeforeEach
    void setupHIBPClient() {
        hibpClient = new HIBPClient(hibpService, Mappers.getMapper(HIBPResponseMapper.class));
    }

    private static Breach generateBreach() {
        return new Breach()
            .setName(BREACH_NAME)
            .setTitle(BREACH_TITLE)
            .setDomain(BREACH_DOMAIN)
            .setDescription("BreachDescription")
            .setLogoPath("logoPath")
            .setDataClasses(asList(DATA_CLASS_1, DATA_CLASS_2))
            .setPwnCount(50)
            .setBreachDate(BREACH_DATE)
            .setAddedDate(ADDED_DATE)
            .setModifiedDate(MODIFIED_DATE)
            .setRetired(true)
            .setVerified(true)
            .setFabricated(true)
            .setSensitive(true)
            .setSpamList(true);
    }

    private static Paste generatePaste() {
        return new Paste()
            .setId("PasteId")
            .setSource("PasteSource")
            .setTitle(PASTE_TITLE)
            .setDate(PASTE_DATE)
            .setEmailCount(50);
    }

    @Test
    void shouldReturnListOfBreachInformationWhenBreachesFound() {
        Breach breach = generateBreach();
        BreachInformation expectedBreahcInformation = new BreachInformation()
            .setDomain(BREACH_DOMAIN)
            .setName(BREACH_NAME)
            .setBreachDate(BREACH_DATE)
            .setInformationBreached(asList(DATA_CLASS_1, DATA_CLASS_2));
        when(hibpService.getBreaches(EMAIL)).thenReturn(singletonList(breach));

        List<BreachInformation> breachInformation = hibpClient.getBreachInformation(EMAIL);

        verify(hibpService).getBreaches(EMAIL);
        assertThat(breachInformation)
            .hasSize(1)
            .contains(expectedBreahcInformation);
    }

    @Test
    void shouldReturnEmptyListWhenNoBreachesFound() {
        when(hibpService.getBreaches(EMAIL)).thenThrow(new HIBPNotFoundException("Not Found"));

        List<BreachInformation> breachInformation = hibpClient.getBreachInformation(EMAIL);

        verify(hibpService).getBreaches(EMAIL);
        assertThat(breachInformation).isEmpty();
    }

    @Test
    void shouldThrowHIBPTooManyRequestsExceptionWhenBreachAPIRateLimited() {
        when(hibpService.getBreaches(EMAIL)).thenThrow(new HIBPTooManyRequestsException(singletonMap(RETRY_AFTER, singletonList("2")), "Too Many Requests"));

        assertThrows(HIBPTooManyRequestsException.class, () -> hibpClient.getBreachInformation(EMAIL));

        verify(hibpService).getBreaches(EMAIL);
    }

    @Test
    void shouldReturnListOfPastesInformationWhenPastesFound() {
        Paste paste = generatePaste();
        PasteInformation expectedPasteInformation = new PasteInformation()
            .setTitle(PASTE_TITLE)
            .setAdded(PASTE_DATE);
        when(hibpService.getPastes(EMAIL)).thenReturn(singletonList(paste));

        List<PasteInformation> pasteInformation = hibpClient.getPasteInformation(EMAIL);

        verify(hibpService).getPastes(EMAIL);
        assertThat(pasteInformation)
            .hasSize(1)
            .contains(expectedPasteInformation);
    }

    @Test
    void shouldReturnEmptyListWhenNoPastesFound() {
        when(hibpService.getPastes(EMAIL)).thenThrow(new HIBPNotFoundException("Not Found"));

        List<PasteInformation> pasteInformation = hibpClient.getPasteInformation(EMAIL);

        verify(hibpService).getPastes(EMAIL);
        assertThat(pasteInformation).isEmpty();
    }

    @Test
    void shouldThrowHIBPTooManyRequestsExceptionWhenPasteAPIRateLimited() {
        when(hibpService.getPastes(EMAIL)).thenThrow(new HIBPTooManyRequestsException(singletonMap(RETRY_AFTER, singletonList("2")), "Too Many Requests"));

        assertThrows(HIBPTooManyRequestsException.class, () -> hibpClient.getPasteInformation(EMAIL));

        verify(hibpService).getPastes(EMAIL);
    }
}
