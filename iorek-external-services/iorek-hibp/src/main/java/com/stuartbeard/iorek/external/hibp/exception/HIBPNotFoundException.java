package com.stuartbeard.iorek.external.hibp.exception;

import feign.error.FeignExceptionConstructor;
import feign.error.ResponseBody;

public class HIBPNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    @FeignExceptionConstructor
    public HIBPNotFoundException(@ResponseBody String body) {
        super(body);
    }
}
