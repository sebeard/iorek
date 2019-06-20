package com.stuartbeard.iorek.external.hibp.exception;

import feign.error.FeignExceptionConstructor;
import feign.error.ResponseBody;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PwnedPasswordsServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    @FeignExceptionConstructor
    public PwnedPasswordsServiceException(@ResponseBody String body) {
        super(body);
    }
}
