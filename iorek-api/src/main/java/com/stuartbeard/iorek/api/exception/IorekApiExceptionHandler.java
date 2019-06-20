package com.stuartbeard.iorek.api.exception;

import com.stuartbeard.iorek.api.model.Message;
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
    public ResponseEntity<Message> handle(HIBPTooManyRequestsException exception) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(new Message(exception.getMessage()));
    }

    @ExceptionHandler(HIBPServiceException.class)
    public ResponseEntity<Message> handle(HIBPServiceException exception) {
        return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(new Message(exception.getMessage()));
    }

    @ExceptionHandler(PwnedPasswordsServiceException.class)
    public ResponseEntity<Message> handle(PwnedPasswordsServiceException exception) {
        return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(new Message(exception.getMessage()));
    }


}
