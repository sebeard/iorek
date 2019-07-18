package com.stuartbeard.iorek.external.hibp.exception;

import feign.error.FeignExceptionConstructor;
import feign.error.ResponseBody;

public class HIBPBadRequestException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    @FeignExceptionConstructor
    public HIBPBadRequestException(@ResponseBody String body) {
        super(body);
    }
}
