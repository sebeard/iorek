package com.stuartbeard.iorek.api.exception;

import com.stuartbeard.iorek.api.model.Message;
import com.stuartbeard.iorek.external.hibp.exception.*;
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

    @ExceptionHandler(HIBPBadRequestException.class)
    public ResponseEntity<Message> handle(HIBPBadRequestException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Message(exception.getMessage()));
    }

    @ExceptionHandler(HIBPNotFoundException.class)
    public ResponseEntity<Message> handle(HIBPNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Message(exception.getMessage()));
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
