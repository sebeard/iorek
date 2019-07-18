package com.stuartbeard.iorek.external.hibp;

import com.stuartbeard.iorek.external.hibp.exception.HIBPNotFoundException;
import com.stuartbeard.iorek.external.hibp.exception.HIBPTooManyRequestsException;
import com.stuartbeard.iorek.external.hibp.model.Breach;
import com.stuartbeard.iorek.external.hibp.model.Paste;
import com.stuartbeard.iorek.external.hibp.service.HIBPService;
import com.stuartbeard.iorek.external.hibp.service.PwnedPasswordsService;
import com.stuartbeard.iorek.service.model.BreachInformation;
import com.stuartbeard.iorek.service.model.PasteInformation;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static feign.Util.RETRY_AFTER;
import static java.util.Arrays.asList;
import static java.util.Collections.*;
import static org.apache.commons.codec.digest.DigestUtils.sha1Hex;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@MockitoSettings
class HIBPClientTest {

    private static final Function<String, String> HASH_FUNCTION = DigestUtils::sha1Hex;
    private static final String PASSWORD = "password";
    private static final String EMAIL = "someone@test.com";
    private static final int PREFIX_LENGTH = 5;
    private static final String PASTE_TITLE = "PasteTitle";
    private static final String BREACH_NAME = "BreachName";
    private static final String BREACH_TITLE = "BreachTitle";
    private static final String BREACH_DOMAIN = "BreachDomain";
    private static final String DATA_CLASS_1 = "DataClass1";
    private static final String DATA_CLASS_2 = "DataClass2";
    private static final Date BREACH_DATE = Date.from(Instant.now());
    private static final Date ADDED_DATE = Date.from(Instant.now());
    private static final Date MODIFIED_DATE = Date.from(Instant.now());
    private static final Date PASTE_DATE = Date.from(Instant.now());

    @Mock
    private PwnedPasswordsService pwnedPasswordsService;

    @Mock
    private HIBPService hibpService;

    private HIBPClient hibpClient;

    private static String hashPrefix() {
        return HASH_FUNCTION.apply(PASSWORD).substring(0, PREFIX_LENGTH).toUpperCase();
    }

    private static String hashSuffix() {
        return HASH_FUNCTION.apply(PASSWORD).substring(PREFIX_LENGTH).toUpperCase();
    }

    @BeforeEach
    void setupHIBPClient() {
        hibpClient = new HIBPClient(pwnedPasswordsService, hibpService, HASH_FUNCTION, PREFIX_LENGTH);
    }

    private static Stream<Arguments> inputs() {
        return Stream.of(
            Arguments.of(PASSWORD, false),
            Arguments.of(sha1Hex(PASSWORD), true)
        );
    }

    @ParameterizedTest
    @MethodSource("inputs")
    void shouldReturnCountWhenPasswordHashFoundInDataSet(String input, boolean sha1Hash) {
        when(pwnedPasswordsService.getMatchingSuffixes(hashPrefix())).thenReturn(singletonList(hashSuffix() + ":999999"));

        int count = hibpClient.getAppearanceCount(input, sha1Hash);

        verify(pwnedPasswordsService).getMatchingSuffixes(hashPrefix());
        assertThat(count, is(999999));
    }

    @ParameterizedTest
    @MethodSource("inputs")
    void shouldReturnZeroWhenPasswordHashNotFoundInDataSet(String input, boolean sha1Hash) {
        when(pwnedPasswordsService.getMatchingSuffixes(hashPrefix())).thenReturn(emptyList());

        int count = hibpClient.getAppearanceCount(input, sha1Hash);

        verify(pwnedPasswordsService).getMatchingSuffixes(hashPrefix());
        assertThat(count, is(0));
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
        assertThat(breachInformation, hasSize(1));
        assertThat(breachInformation, contains(expectedBreahcInformation));
    }

    @Test
    void shouldReturnEmptyListWhenNoBreachesFound() {
        when(hibpService.getBreaches(EMAIL)).thenThrow(new HIBPNotFoundException("Not Found"));

        List<BreachInformation> breachInformation = hibpClient.getBreachInformation(EMAIL);

        verify(hibpService).getBreaches(EMAIL);
        assertThat(breachInformation, hasSize(0));
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
        assertThat(pasteInformation, hasSize(1));
        assertThat(pasteInformation, contains(expectedPasteInformation));
    }

    @Test
    void shouldReturnEmptyListWhenNoPastesFound() {
        when(hibpService.getPastes(EMAIL)).thenThrow(new HIBPNotFoundException("Not Found"));

        List<PasteInformation> pasteInformation = hibpClient.getPasteInformation(EMAIL);

        verify(hibpService).getPastes(EMAIL);
        assertThat(pasteInformation, hasSize(0));
    }

    @Test
    void shouldThrowHIBPTooManyRequestsExceptionWhenPasteAPIRateLimited() {
        when(hibpService.getPastes(EMAIL)).thenThrow(new HIBPTooManyRequestsException(singletonMap(RETRY_AFTER, singletonList("2")), "Too Many Requests"));

        assertThrows(HIBPTooManyRequestsException.class, () -> hibpClient.getPasteInformation(EMAIL));

        verify(hibpService).getPastes(EMAIL);
    }
}
