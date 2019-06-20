package com.stuartbeard.iorek.external.hibp.exception;

import feign.error.FeignExceptionConstructor;
import feign.error.ResponseBody;
import feign.error.ResponseHeaders;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
public class HIBPTooManyRequestsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private int retryAfter = 0;

    @FeignExceptionConstructor
    public HIBPTooManyRequestsException(@ResponseHeaders Map<String, String> headers, @ResponseBody String body) {
        super(body);
        retryAfter = Integer.parseInt(headers.getOrDefault("Retry-After", "0"));
    }
}
