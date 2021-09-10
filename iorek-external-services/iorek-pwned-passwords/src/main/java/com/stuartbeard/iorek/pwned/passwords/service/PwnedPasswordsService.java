/*
 * MIT License
 *
 * Copyright (c) 2019-2021 Stuart Beard
 */
package com.stuartbeard.iorek.pwned.passwords.service;

import com.stuartbeard.iorek.pwned.passwords.exception.PwnedPasswordsServiceException;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import feign.error.ErrorHandling;

import java.util.List;

/**
 * Feign generated (via annotations) API integration for PwnedPasswords external service
 *
 * @author Stuart Beard
 * @version 1.0.0
 * @since 1.0.0
 */
public interface PwnedPasswordsService {

    /**
     * Calls the PwnedPwasswords service on <code>GET /range/{sha1Prefix}</code> to retrieve a list of matching
     * suffixes. Any exception/error response is translated into a default exception (not ever expected based on the
     * architecturew of PwnedPasswords). Uses Iorek as User agent (for Identification) and enables Padding to ensure
     * that all responses are uniformly shaped to avoid sniffing.
     *
     * @param sha1Prefix The required k-anonymity SHA-1 prefix length
     * @return A list of matching suffixes along with the counts of how many times that suffix has been seen in a known credential breach. Colon separated
     */
    @ErrorHandling(defaultException = PwnedPasswordsServiceException.class)
    @Headers({
        "User-Agent: Iorek",
        "Add-Padding: true"
    })
    @RequestLine("GET /range/{sha1Prefix}")
    List<String> getMatchingSuffixes(@Param("sha1Prefix") String sha1Prefix);

}
