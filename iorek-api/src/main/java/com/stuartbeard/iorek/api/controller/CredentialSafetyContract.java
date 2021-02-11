package com.stuartbeard.iorek.api.controller;

import com.stuartbeard.iorek.service.model.CredentialSafety;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import static com.stuartbeard.iorek.api.descriptions.APIDescriptions.API_200_MESSAGE;
import static com.stuartbeard.iorek.api.descriptions.APIDescriptions.API_429_MESSAGE;
import static com.stuartbeard.iorek.api.descriptions.APIDescriptions.API_500_MESSAGE;
import static com.stuartbeard.iorek.api.descriptions.APIDescriptions.API_503_MESSAGE;
import static com.stuartbeard.iorek.api.descriptions.APIDescriptions.API_504_MESSAGE;
import static com.stuartbeard.iorek.api.descriptions.APIDescriptions.GET_CREDENTIAL_SAFETY_DESC;

public interface CredentialSafetyContract {

    @ApiOperation(value = GET_CREDENTIAL_SAFETY_DESC, response = CredentialSafety.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = API_200_MESSAGE, response = CredentialSafety.class),
        @ApiResponse(code = 429, message = API_429_MESSAGE, response = String.class, responseHeaders =
        @ResponseHeader(name = HttpHeaders.RETRY_AFTER)),
        @ApiResponse(code = 500, message = API_500_MESSAGE, response = String.class),
        @ApiResponse(code = 503, message = API_503_MESSAGE, response = String.class, responseHeaders =
        @ResponseHeader(name = HttpHeaders.RETRY_AFTER)),
        @ApiResponse(code = 504, message = API_504_MESSAGE, response = String.class, responseHeaders =
        @ResponseHeader(name = HttpHeaders.RETRY_AFTER))
    })
    CredentialSafety getCredentialSafety(@PathVariable("credential") String credential,
                                         @RequestParam(value = "sha1Hash", defaultValue = "true") boolean isSha1Hash);
}
