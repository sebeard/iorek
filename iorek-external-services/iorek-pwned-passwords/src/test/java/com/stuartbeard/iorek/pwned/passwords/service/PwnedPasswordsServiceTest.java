/*
 * MIT License
 *
 * Copyright (c) 2019-2021 Stuart Beard
 */
package com.stuartbeard.iorek.pwned.passwords.service;

import feign.Feign;
import com.stuartbeard.iorek.pwned.passwords.decoder.CLRFStringDecoder;
import com.stuartbeard.iorek.pwned.passwords.exception.PwnedPasswordsServiceException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Function;

import static feign.error.AnnotationErrorDecoder.builderFor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PwnedPasswordsServiceTest {

    private static final Function<String, String> HASH_FUNCTION = DigestUtils::sha1Hex;
    private static final String PASSWORD = "password";
    private static final int PREFIX_LENGTH = 5;
    private static final String exampleSuffixes = "003D68EB55068C33ACE09247EE4C639306B:3\r\n" +
        "1595A8D396AC6F7941A84D6F7100B1A7C5C:9\r\n" +
        "1E4C9B93F3F0682250B6CF8331B7EE68FD8:3645804\r\n" +
        "1F2B668E8AABEF1C59E9EC6F82E3F3CD786:1\r\n" +
        "2648FB0B2EDA4FDFF99BF51E912CD95C023:7059";

    private final MockWebServer pwnedPasswordsAPI = new MockWebServer();

    private PwnedPasswordsService pwnedPasswordsService;

    private static String hashPrefix() {
        return HASH_FUNCTION.apply(PASSWORD).substring(0, PREFIX_LENGTH).toUpperCase();
    }

    @BeforeEach
    void setupRestClient() {
        pwnedPasswordsService = Feign.builder()
            .errorDecoder(builderFor(PwnedPasswordsService.class).build())
            .decoder(new CLRFStringDecoder())
            .target(PwnedPasswordsService.class, pwnedPasswordsAPI.url("").toString());
    }

    @Test
    void shouldThrowPwnedPasswordExceptionWhenServiceUnavailable() {
        pwnedPasswordsAPI.enqueue(new MockResponse()
            .setResponseCode(503));
        String sha1Prefix = hashPrefix();

        assertThrows(PwnedPasswordsServiceException.class, () -> pwnedPasswordsService.getMatchingSuffixes(sha1Prefix));
    }

    @Test
    void shouldReturnListOfHashesSuccessfully() {
        String expectedSuffix = "1E4C9B93F3F0682250B6CF8331B7EE68FD8:3645804";
        pwnedPasswordsAPI.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(exampleSuffixes));

        List<String> matchingSuffixes = pwnedPasswordsService.getMatchingSuffixes(hashPrefix());

        assertThat(matchingSuffixes)
            .contains(expectedSuffix)
            .hasSize(5);
    }


}
