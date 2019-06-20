package com.stuartbeard.iorek.external.hibp.service;

import com.stuartbeard.iorek.external.hibp.exception.HIBPServiceException;
import com.stuartbeard.iorek.external.hibp.exception.HIBPTooManyRequestsException;
import com.stuartbeard.iorek.external.hibp.model.Breach;
import com.stuartbeard.iorek.external.hibp.model.Paste;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import feign.error.ErrorCodes;
import feign.error.ErrorHandling;

import java.util.List;

public interface HIBPService {

    @ErrorHandling(
        codeSpecific = {
            @ErrorCodes(codes = 429, generate = HIBPTooManyRequestsException.class)
        },
        defaultException = HIBPServiceException.class)
    @Headers("User-Agent: Iorek")
    @RequestLine("GET /breachedaccount/{account}")
    List<Breach> getBreaches(@Param("account") String email) throws HIBPServiceException, HIBPTooManyRequestsException;

    @ErrorHandling(
        codeSpecific = {
            @ErrorCodes(codes = 429, generate = HIBPTooManyRequestsException.class)
        },
        defaultException = HIBPServiceException.class)
    @Headers("User-Agent: Iorek")
    @RequestLine("GET /pasteaccount/{account}")
    List<Paste> getPastes(@Param("account") String username) throws HIBPServiceException, HIBPTooManyRequestsException;

}
