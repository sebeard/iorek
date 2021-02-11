package com.stuartbeard.iorek.external.hibp.exception;

import feign.codec.RetryAfterException;
import feign.error.FeignExceptionConstructor;
import feign.error.ResponseBody;
import feign.error.ResponseHeaders;

import java.util.Collection;
import java.util.Map;

public class HIBPTooManyRequestsException extends RetryAfterException {

    private static final long serialVersionUID = 1L;

    @FeignExceptionConstructor
    public HIBPTooManyRequestsException(@ResponseHeaders Map<String, Collection<String>> headers, @ResponseBody String errorMessage) {
        super(429, headers, errorMessage);
    }
}

