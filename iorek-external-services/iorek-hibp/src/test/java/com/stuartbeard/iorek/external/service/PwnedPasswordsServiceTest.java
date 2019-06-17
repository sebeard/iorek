package com.stuartbeard.iorek.external.service;

import com.stuartbeard.iorek.external.hibp.exception.PwnedPasswordsServiceException;
import com.stuartbeard.iorek.external.hibp.service.PwnedPasswordsService;
import feign.Feign;
import feign.codec.StringDecoder;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Function;

import static feign.error.AnnotationErrorDecoder.builderFor;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PwnedPasswordsServiceTest {

    private static final Function<String, String> HASH_FUNCTION = DigestUtils::sha1Hex;
    private static final String PASSWORD = "password";
    private static final int PREFIX_LENGTH = 5;

    private MockWebServer pwnedPasswordsAPI = new MockWebServer();

    private PwnedPasswordsService pwnedPasswordsService;

    private static String hashPrefix() {
        return HASH_FUNCTION.apply(PASSWORD).substring(0, PREFIX_LENGTH).toUpperCase();
    }

    @BeforeEach
    void setupRestClient() {
        pwnedPasswordsService = Feign.builder()
            .errorDecoder(builderFor(PwnedPasswordsService.class).build())
            .decoder(new StringDecoder())
            .target(PwnedPasswordsService.class, pwnedPasswordsAPI.url("").toString());
    }

    @Test
    void shouldReturnListOfHashesSuccessfully() {
        String expectedSuffixes = "";
        pwnedPasswordsAPI.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(expectedSuffixes));

        List<String> matchingSuffixes = pwnedPasswordsService.getMatchingSuffixes(hashPrefix());

        assertThat(matchingSuffixes, hasItem(expectedSuffixes));
    }

    @Test
    void shouldThrowPwnedPasswordExcpetionWhenServiceUnavailable() {
        pwnedPasswordsAPI.enqueue(new MockResponse()
            .setResponseCode(503));

        assertThrows(PwnedPasswordsServiceException.class, () -> pwnedPasswordsService.getMatchingSuffixes(hashPrefix()));
    }
}
