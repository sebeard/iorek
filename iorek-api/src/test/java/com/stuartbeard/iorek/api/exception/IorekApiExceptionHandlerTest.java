package com.stuartbeard.iorek.api.exception;

import com.stuartbeard.iorek.api.model.ErrorResponse;
import com.stuartbeard.iorek.external.hibp.exception.HIBPBadRequestException;
import com.stuartbeard.iorek.external.hibp.exception.HIBPNotFoundException;
import com.stuartbeard.iorek.external.hibp.exception.HIBPServiceException;
import com.stuartbeard.iorek.external.hibp.exception.HIBPTooManyRequestsException;
import com.stuartbeard.iorek.external.hibp.exception.PwnedPasswordsServiceException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class IorekApiExceptionHandlerTest {

    private static final String MESSAGE = "It's a trap";

    private final IorekApiExceptionHandler handler = new IorekApiExceptionHandler();

    @Test
    void shouldHandleHIBPServiceException() {
        HIBPServiceException exception = new HIBPServiceException(MESSAGE);

        ResponseEntity<ErrorResponse> response = handler.handle(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.GATEWAY_TIMEOUT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo(MESSAGE);
    }

    @Test
    void shouldHandleHIBPTooManyRequestsException() {
        HIBPTooManyRequestsException exception = new HIBPTooManyRequestsException(Collections.singletonMap(HttpHeaders.RETRY_AFTER, Collections.singleton("2")), MESSAGE);

        ResponseEntity<ErrorResponse> response = handler.handle(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.TOO_MANY_REQUESTS);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo(MESSAGE);
    }

    @Test
    void shouldHandleHIBPBadRequestException() {
        HIBPBadRequestException exception = new HIBPBadRequestException(MESSAGE);

        ResponseEntity<ErrorResponse> response = handler.handle(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo(MESSAGE);
    }

    @Test
    void shouldHandleHIBPNotFoundException() {
        HIBPNotFoundException exception = new HIBPNotFoundException(MESSAGE);

        ResponseEntity<ErrorResponse> response = handler.handle(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo(MESSAGE);
    }

    @Test
    void shouldHandlePwnedPasswordsServiceException() {
        PwnedPasswordsServiceException exception = new PwnedPasswordsServiceException(MESSAGE);

        ResponseEntity<ErrorResponse> response = handler.handle(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.GATEWAY_TIMEOUT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo(MESSAGE);
    }
}
