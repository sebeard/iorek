package com.stuartbeard.iorek.external.hibp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stuartbeard.iorek.external.hibp.exception.HIBPNotFoundException;
import com.stuartbeard.iorek.external.hibp.exception.HIBPServiceException;
import com.stuartbeard.iorek.external.hibp.exception.HIBPTooManyRequestsException;
import com.stuartbeard.iorek.external.hibp.model.Breach;
import com.stuartbeard.iorek.external.hibp.model.Paste;
import feign.Client;
import feign.Feign;
import feign.Request;
import feign.jackson.JacksonDecoder;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoSettings;

import java.io.IOException;
import java.util.List;

import static feign.Util.RETRY_AFTER;
import static feign.error.AnnotationErrorDecoder.builderFor;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@MockitoSettings
class HIBPServiceTest {

    private static final String EMAIL = "someone@test.com";

    @Spy
    Client client = new Client.Default(null, null);

    private final MockWebServer hibpApi = new MockWebServer();
    private HIBPService hibpService;

    private static Breach generateBreach() {
        return new Breach();
    }

    private static Paste generatePaste() {
        return new Paste();
    }

    @BeforeEach
    void setupRestClient() {
        hibpService = Feign.builder()
            .client(client)
            .errorDecoder(builderFor(HIBPService.class).build())
            .decoder(new JacksonDecoder())
            .target(HIBPService.class, hibpApi.url("").toString());
    }

    @Test
    void shouldThrowHIBPServiceExceptionWhenBreachServiceUnavailable() {
        hibpApi.enqueue(new MockResponse()
            .setResponseCode(503));

        assertThrows(HIBPServiceException.class, () -> hibpService.getBreaches(EMAIL));
    }

    @Test
    void shouldThrowHIBPServiceExceptionWhenPasteServiceUnavailable() {
        hibpApi.enqueue(new MockResponse()
            .setResponseCode(503));

        assertThrows(HIBPServiceException.class, () -> hibpService.getPastes(EMAIL));
    }

    @Test
    void shouldThrowHIBPNotFoundExceptionWhenEmailNotFoundInBreachService() {
        hibpApi.enqueue(new MockResponse()
            .setResponseCode(404));

        assertThrows(HIBPNotFoundException.class, () -> hibpService.getBreaches(EMAIL));
    }

    @Test
    void shouldThrowHIBPNotFoundExceptionWhenEmailNotFoundInPasteService() {
        hibpApi.enqueue(new MockResponse()
            .setResponseCode(404));

        assertThrows(HIBPNotFoundException.class, () -> hibpService.getPastes(EMAIL));
    }

    @Test
    void shouldThrowHIBPTooManyRequestsExceptionWhenBreachServiceIsRateLimited() throws IOException {
        MockResponse tooManyRequestsResponse = new MockResponse()
            .addHeader(RETRY_AFTER, 2)
            .setResponseCode(429);
        hibpApi.enqueue(tooManyRequestsResponse);
        hibpApi.enqueue(tooManyRequestsResponse);
        hibpApi.enqueue(tooManyRequestsResponse);
        hibpApi.enqueue(tooManyRequestsResponse);
        hibpApi.enqueue(tooManyRequestsResponse);

        assertThrows(HIBPTooManyRequestsException.class, () -> hibpService.getBreaches(EMAIL));

        verify(client, times(5)).execute(any(Request.class), any(Request.Options.class));
    }

    @Test
    void shouldThrowHIBPTooManyRequestsExceptionWhenPasteServiceIsRateLimited() throws IOException {
        MockResponse tooManyRequestsResponse = new MockResponse()
            .addHeader(RETRY_AFTER, 2)
            .setResponseCode(429);
        hibpApi.enqueue(tooManyRequestsResponse);
        hibpApi.enqueue(tooManyRequestsResponse);
        hibpApi.enqueue(tooManyRequestsResponse);
        hibpApi.enqueue(tooManyRequestsResponse);
        hibpApi.enqueue(tooManyRequestsResponse);

        assertThrows(HIBPTooManyRequestsException.class, () -> hibpService.getPastes(EMAIL));

        verify(client, times(5)).execute(any(Request.class), any(Request.Options.class));
    }

    @Test
    void shouldReturnListOfBreachesSuccessfully() throws JsonProcessingException {
        Breach breach1 = generateBreach();
        Breach breach2 = generateBreach();
        Breach breach3 = generateBreach();
        List<Breach> apiBreaches = asList(breach1, breach2, breach3);
        hibpApi.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(new ObjectMapper().writeValueAsString(apiBreaches)));

        List<Breach> breaches = hibpService.getBreaches(EMAIL);

        assertThat(breaches)
            .hasSize(3)
            .contains(breach1, breach2, breach3);
    }

    @Test
    void shouldReturnListOfPastesSuccessfully() throws JsonProcessingException {
        Paste paste1 = generatePaste();
        Paste paste2 = generatePaste();
        Paste paste3 = generatePaste();
        List<Paste> apiPastes = asList(paste1, paste2, paste3);
        hibpApi.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(new ObjectMapper().writeValueAsString(apiPastes)));

        List<Paste> pastes = hibpService.getPastes(EMAIL);

        assertThat(pastes)
            .hasSize(3)
            .contains(paste1, paste2, paste3);
    }


}
