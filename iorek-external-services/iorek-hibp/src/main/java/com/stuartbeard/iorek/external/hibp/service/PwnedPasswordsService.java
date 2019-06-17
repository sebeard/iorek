package com.stuartbeard.iorek.external.hibp.service;

import com.stuartbeard.iorek.external.hibp.exception.PwnedPasswordsServiceException;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import feign.error.ErrorHandling;

import java.util.List;

public interface PwnedPasswordsService {

    @ErrorHandling(defaultException = PwnedPasswordsServiceException.class)
    @Headers("User-Agent: Iorek")
    @RequestLine("GET /range/{sha1Prefix}")
    List<String> getMatchingSuffixes(@Param("sha1Prefix") String sha1Prefix) throws PwnedPasswordsServiceException;

}
