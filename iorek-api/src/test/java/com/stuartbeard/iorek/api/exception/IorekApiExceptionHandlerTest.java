package com.stuartbeard.iorek.api.exception;

import com.stuartbeard.iorek.api.model.Message;
import com.stuartbeard.iorek.external.hibp.exception.HIBPServiceException;
import com.stuartbeard.iorek.external.hibp.exception.HIBPTooManyRequestsException;
import com.stuartbeard.iorek.external.hibp.exception.PwnedPasswordsServiceException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

class IorekApiExceptionHandlerTest {

    private static final String MESSAGE = "It's a trap";

    private IorekApiExceptionHandler handler = new IorekApiExceptionHandler();

    @Test
    void shouldHandleHIBPServiceException() {
        HIBPServiceException exception = new HIBPServiceException(MESSAGE);

        ResponseEntity<Message> response = handler.handle(exception);

        assertThat(response.getStatusCode(), is(HttpStatus.GATEWAY_TIMEOUT));
        assertThat(response.getBody(), is(notNullValue()));
        assertThat(response.getBody().getMessage(), is(MESSAGE));
    }

    @Test
    void shouldHandleHIBPTooManyRequestsException() {
        HIBPTooManyRequestsException exception = new HIBPTooManyRequestsException(Collections.singletonMap(HttpHeaders.RETRY_AFTER, "2"), MESSAGE);

        ResponseEntity<Message> response = handler.handle(exception);

        assertThat(response.getStatusCode(), is(HttpStatus.TOO_MANY_REQUESTS));
        assertThat(response.getBody(), is(notNullValue()));
        assertThat(response.getBody().getMessage(), is(MESSAGE));
    }

    @Test
    void shouldHandlePwnedPasswordsServiceException() {
        PwnedPasswordsServiceException exception = new PwnedPasswordsServiceException(MESSAGE);

        ResponseEntity<Message> response = handler.handle(exception);

        assertThat(response.getStatusCode(), is(HttpStatus.GATEWAY_TIMEOUT));
        assertThat(response.getBody(), is(notNullValue()));
        assertThat(response.getBody().getMessage(), is(MESSAGE));
    }
}
