package com.stuartbeard.iorek.external.hibp.decoder;

import feign.Request;
import feign.Response;
import feign.codec.DecodeException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CLRFStringDecoderTest {

    private final CLRFStringDecoder decoder = new CLRFStringDecoder();

    private final Response response = Response.builder()
        .request(Request.create(Request.HttpMethod.GET, "localhost", Collections.emptyMap(), null))
        .status(200)
        .body("Line 1\r\nLine 2", StandardCharsets.UTF_8)
        .build();

    @Test
    void shouldDecodeNull() throws IOException {
        Response response = Response.builder()
            .request(Request.create(Request.HttpMethod.GET, "localhost", Collections.emptyMap(), null))
            .status(200)
            .body(null, StandardCharsets.UTF_8)
            .build();

        Object object = decoder.decode(response, String.class);

        assertThat(object).isNull();
    }

    @Test
    void shouldDecodeString() throws IOException {
        Object object = decoder.decode(response, String.class);

        assertThat(object)
            .isInstanceOf(String.class)
            .isEqualTo("Line 1\r\nLine 2");
    }

    @Test
    void shouldDecodeToStringArray() throws IOException {
        Object object = decoder.decode(response, String[].class);

        assertThat(object).isInstanceOf(String[].class);
        assertThat(((String[]) object)).hasSize(2).containsExactly("Line 1", "Line 2");
    }

    @Test
    void shouldThrowExceptionWhenTypeUnsupported() {
        DecodeException exception = assertThrows(DecodeException.class, () -> decoder.decode(response, Integer.class));

        assertThat(exception.status()).isEqualTo(200);
        assertThat(exception.getMessage()).isEqualTo("class java.lang.Integer is not a type supported by this decoder.");
    }


}
