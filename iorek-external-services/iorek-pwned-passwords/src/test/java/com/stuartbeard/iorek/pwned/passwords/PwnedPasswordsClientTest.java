/*
 * MIT License
 *
 * Copyright (c) 2019-2021 Stuart Beard
 */
package com.stuartbeard.iorek.pwned.passwords;

import com.stuartbeard.iorek.pwned.passwords.service.PwnedPasswordsService;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.apache.commons.codec.digest.DigestUtils.sha1Hex;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@MockitoSettings
class PwnedPasswordsClientTest {

    private static final UnaryOperator<String> HASH_FUNCTION = DigestUtils::sha1Hex;
    private static final String PASSWORD = "password";
    private static final int PREFIX_LENGTH = 5;

    @Mock
    private PwnedPasswordsService pwnedPasswordsService;

    private PwnedPasswordsClient hibpClient;

    private static String hashPrefix() {
        return HASH_FUNCTION.apply(PASSWORD).substring(0, PREFIX_LENGTH).toUpperCase();
    }

    private static String hashSuffix() {
        return HASH_FUNCTION.apply(PASSWORD).substring(PREFIX_LENGTH).toUpperCase();
    }

    private static Stream<Arguments> inputs() {
        return Stream.of(
            Arguments.of(PASSWORD, false),
            Arguments.of(sha1Hex(PASSWORD), true)
        );
    }

    @BeforeEach
    void setupHIBPClient() {
        hibpClient = new PwnedPasswordsClient(pwnedPasswordsService, HASH_FUNCTION, PREFIX_LENGTH);
    }

    @ParameterizedTest
    @MethodSource("inputs")
    void shouldReturnCountWhenPasswordHashFoundInDataSet(String input, boolean sha1Hash) {
        when(pwnedPasswordsService.getMatchingSuffixes(hashPrefix())).thenReturn(singletonList(hashSuffix() + ":999999"));

        int count = hibpClient.getAppearanceCount(input, sha1Hash);

        verify(pwnedPasswordsService).getMatchingSuffixes(hashPrefix());
        assertThat(count).isEqualTo(999999);
    }

    @ParameterizedTest
    @MethodSource("inputs")
    void shouldReturnZeroWhenPasswordHashNotFoundInDataSet(String input, boolean sha1Hash) {
        when(pwnedPasswordsService.getMatchingSuffixes(hashPrefix())).thenReturn(emptyList());

        int count = hibpClient.getAppearanceCount(input, sha1Hash);

        verify(pwnedPasswordsService).getMatchingSuffixes(hashPrefix());
        assertThat(count).isZero();
    }
}
