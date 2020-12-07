package com.stuartbeard.iorek.api.controller;

import com.stuartbeard.iorek.service.BreachCheckService;
import com.stuartbeard.iorek.service.model.IdentityInformation;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static com.stuartbeard.iorek.api.descriptions.APIDescriptions.BREACH_CHECK_NAME;

@RestController
@RequestMapping("breach-check")
@Api(value = BREACH_CHECK_NAME,
    produces = MediaType.APPLICATION_JSON_VALUE,
    tags = {"breaches"}
)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BreachCheckController implements BreachCheckContract {

    private final BreachCheckService breachCheckService;

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public IdentityInformation getIdentityInformation(@RequestParam(value = "email") String emailAddress) {
        return breachCheckService.checkIdentity(emailAddress);
    }
}
