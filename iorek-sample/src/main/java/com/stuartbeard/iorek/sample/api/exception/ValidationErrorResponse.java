package com.stuartbeard.iorek.sample.api.exception;

import lombok.Data;

@Data
public class ValidationErrorResponse {

    private String field;
    private String message;
}
