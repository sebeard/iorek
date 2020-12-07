package com.stuartbeard.iorek.api.exception;

import com.stuartbeard.iorek.api.model.ErrorResponse;
import com.stuartbeard.iorek.external.hibp.exception.HIBPBadRequestException;
import com.stuartbeard.iorek.external.hibp.exception.HIBPNotFoundException;
import com.stuartbeard.iorek.external.hibp.exception.HIBPServiceException;
import com.stuartbeard.iorek.external.hibp.exception.HIBPTooManyRequestsException;
import com.stuartbeard.iorek.external.hibp.exception.PwnedPasswordsServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class IorekApiExceptionHandler {

    @ExceptionHandler(HIBPTooManyRequestsException.class)
    public ResponseEntity<ErrorResponse> handle(HIBPTooManyRequestsException exception) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(HIBPBadRequestException.class)
    public ResponseEntity<ErrorResponse> handle(HIBPBadRequestException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(HIBPNotFoundException.class)
    public ResponseEntity<ErrorResponse> handle(HIBPNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(HIBPServiceException.class)
    public ResponseEntity<ErrorResponse> handle(HIBPServiceException exception) {
        return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(PwnedPasswordsServiceException.class)
    public ResponseEntity<ErrorResponse> handle(PwnedPasswordsServiceException exception) {
        return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(new ErrorResponse(exception.getMessage()));
    }


}
