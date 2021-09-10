/*
 * MIT License
 *
 * Copyright (c) 2019-2021 Stuart Beard
 */
package com.stuartbeard.iorek.pwned.passwords.exception;

import feign.error.FeignExceptionConstructor;
import feign.error.ResponseBody;

/**
 * Generic catch all exception class for any issue encountered when interacting with the PwnedPasswords API.
 *
 * Used by Feign directly to instantiate using the response body as the main message. This is generally not expected
 * to trigger.
 *
 * @author Stuart Beard
 * @version 1.0.0
 * @since 1.0.0
 */
public class PwnedPasswordsServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    @FeignExceptionConstructor
    public PwnedPasswordsServiceException(@ResponseBody String body) {
        super(body);
    }
}
