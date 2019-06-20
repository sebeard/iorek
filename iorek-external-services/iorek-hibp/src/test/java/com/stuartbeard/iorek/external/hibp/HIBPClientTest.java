package com.stuartbeard.iorek.external.hibp;

import com.stuartbeard.iorek.external.hibp.service.HIBPService;
import com.stuartbeard.iorek.external.hibp.service.PwnedPasswordsService;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.function.Function;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@MockitoSettings
class HIBPClientTest {

    private static final Function<String, String> HASH_FUNCTION = DigestUtils::sha1Hex;
    private static final String PASSWORD = "password";
    private static final int PREFIX_LENGTH = 5;

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

    @Test
    void shouldReturnCountWhenPasswordHashFoundInDataSet() {
        when(pwnedPasswordsService.getMatchingSuffixes(hashPrefix())).thenReturn(singletonList(hashSuffix() + ":999999"));

        int count = hibpClient.getAppearanceCount(PASSWORD);

        verify(pwnedPasswordsService).getMatchingSuffixes(hashPrefix());
        assertThat(count, is(999999));
    }

    @Test
    void shouldReturnZeroWhenPasswordHashNotFoundInDataSet() {
        when(pwnedPasswordsService.getMatchingSuffixes(hashPrefix())).thenReturn(emptyList());

        int count = hibpClient.getAppearanceCount(PASSWORD);

        verify(pwnedPasswordsService).getMatchingSuffixes(hashPrefix());
        assertThat(count, is(0));
    }
}
