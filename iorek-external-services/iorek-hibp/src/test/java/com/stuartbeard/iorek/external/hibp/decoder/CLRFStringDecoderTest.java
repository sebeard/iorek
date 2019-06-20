package com.stuartbeard.iorek.external.hibp.decoder;

import feign.Request;
import feign.Response;
import feign.codec.DecodeException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CLRFStringDecoderTest {

    private CLRFStringDecoder decoder = new CLRFStringDecoder();

    private Response response = Response.builder()
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

        assertThat(object, is(nullValue()));
    }

    @Test
    void shouldDecodeString() throws IOException {
        Object object = decoder.decode(response, String.class);

        assertThat(object, is(instanceOf(String.class)));
        assertThat(object, is("Line 1\r\nLine 2"));
    }

    @Test
    void shouldDecodeToStringArray() throws IOException {
        Object object = decoder.decode(response, String[].class);

        assertThat(object, is(instanceOf(String[].class)));
        assertThat(((String[]) object).length, is(2));
        assertThat(((String[]) object)[0], is("Line 1"));
        assertThat(((String[]) object)[1], is("Line 2"));
    }

    @Test
    void shouldThrowExceptionWhenTypeUnsupported() {
        DecodeException exception = assertThrows(DecodeException.class, () -> decoder.decode(response, Integer.class));

        assertThat(exception.status(), is(200));
        assertThat(exception.getMessage(), is("class java.lang.Integer is not a type supported by this decoder."));
    }


}
