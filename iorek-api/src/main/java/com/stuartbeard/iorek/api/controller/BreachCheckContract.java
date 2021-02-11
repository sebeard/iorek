package com.stuartbeard.iorek.api.controller;

import com.stuartbeard.iorek.api.model.ErrorResponse;
import com.stuartbeard.iorek.service.model.IdentityInformation;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestParam;

import static com.stuartbeard.iorek.api.descriptions.APIDescriptions.API_200_MESSAGE;
import static com.stuartbeard.iorek.api.descriptions.APIDescriptions.API_429_MESSAGE;
import static com.stuartbeard.iorek.api.descriptions.APIDescriptions.API_500_MESSAGE;
import static com.stuartbeard.iorek.api.descriptions.APIDescriptions.API_503_MESSAGE;
import static com.stuartbeard.iorek.api.descriptions.APIDescriptions.API_504_MESSAGE;
import static com.stuartbeard.iorek.api.descriptions.APIDescriptions.GET_IDENTITY_INFO_DESC;

public interface BreachCheckContract {

    @ApiOperation(value = GET_IDENTITY_INFO_DESC, response = IdentityInformation.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = API_200_MESSAGE, response = IdentityInformation.class),
        @ApiResponse(code = 429, message = API_429_MESSAGE, response = ErrorResponse.class, responseHeaders =
        @ResponseHeader(name = HttpHeaders.RETRY_AFTER)),
        @ApiResponse(code = 500, message = API_500_MESSAGE, response = ErrorResponse.class),
        @ApiResponse(code = 503, message = API_503_MESSAGE, response = ErrorResponse.class, responseHeaders =
        @ResponseHeader(name = HttpHeaders.RETRY_AFTER)),
        @ApiResponse(code = 504, message = API_504_MESSAGE, response = ErrorResponse.class, responseHeaders =
        @ResponseHeader(name = HttpHeaders.RETRY_AFTER))
    })
    IdentityInformation getIdentityInformation(@RequestParam(value = "email") String emailAddress);
}
