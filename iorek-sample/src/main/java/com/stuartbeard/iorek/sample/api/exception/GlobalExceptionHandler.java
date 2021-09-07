package com.stuartbeard.iorek.sample.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Request Validation Exceptions
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public List<ValidationErrorResponse> handleException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        return bindingResult
            .getAllErrors()
            .stream()
            .map(br -> {
                ValidationErrorResponse validationErrorResponse = new ValidationErrorResponse();
                validationErrorResponse.setMessage(br.getDefaultMessage());

                if (br instanceof FieldError) {
                    FieldError fieldError = (FieldError) br;
                    validationErrorResponse.setField(fieldError.getField());
                    return validationErrorResponse;
                }

                return validationErrorResponse;
            })
            .collect(Collectors.toList());
    }


}
