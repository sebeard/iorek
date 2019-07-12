package com.stuartbeard.iorek.api.controller;

import com.stuartbeard.iorek.service.PasswordCheckingService;
import com.stuartbeard.iorek.service.model.CredentialSafety;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static com.stuartbeard.iorek.api.descriptions.APIDescriptions.*;

@RestController
@RequestMapping("credential-safety")
@Api(value = "Credential Safety Endpoint",
    produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
    description = GET_CREDENTIAL_SAFETY_DESC,
    tags = {"credentials"}
)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CredentialSafetyController {

    private PasswordCheckingService passwordCheckingService;

    @GetMapping(value = "/{credential}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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
    @ResponseBody
    public CredentialSafety getCredentialSafety(@PathVariable("credential") String credential,
                                                @RequestParam(value = "sha1Hash", defaultValue = "true") boolean isSha1Hash) {
        return passwordCheckingService.checkCredentialSafetyInfo(credential, isSha1Hash);
    }
}
