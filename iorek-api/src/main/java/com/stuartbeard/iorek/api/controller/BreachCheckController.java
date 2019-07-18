package com.stuartbeard.iorek.api.controller;

import com.stuartbeard.iorek.api.model.Message;
import com.stuartbeard.iorek.service.BreachCheckService;
import com.stuartbeard.iorek.service.model.IdentityInformation;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static com.stuartbeard.iorek.api.descriptions.APIDescriptions.*;

@RestController
@RequestMapping("breach-check")
@Api(value = BREACH_CHECK_NAME,
    produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
    description = BREACH_CHECK_DESC,
    tags = {"breaches"}
)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BreachCheckController {

    private BreachCheckService breachCheckService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = GET_IDENTITY_INFO_DESC, response = IdentityInformation.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = API_200_MESSAGE, response = IdentityInformation.class),
        @ApiResponse(code = 429, message = API_429_MESSAGE, response = Message.class, responseHeaders =
        @ResponseHeader(name = HttpHeaders.RETRY_AFTER)),
        @ApiResponse(code = 500, message = API_500_MESSAGE, response = Message.class),
        @ApiResponse(code = 503, message = API_503_MESSAGE, response = Message.class, responseHeaders =
        @ResponseHeader(name = HttpHeaders.RETRY_AFTER)),
        @ApiResponse(code = 504, message = API_504_MESSAGE, response = Message.class, responseHeaders =
        @ResponseHeader(name = HttpHeaders.RETRY_AFTER))
    })
    @ResponseBody
    public IdentityInformation getIdentityInformation(@RequestParam(value = "email") String emailAddress) {
        return breachCheckService.checkIdentity(emailAddress);
    }
}
