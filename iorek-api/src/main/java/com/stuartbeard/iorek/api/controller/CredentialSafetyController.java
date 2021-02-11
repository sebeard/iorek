package com.stuartbeard.iorek.api.controller;

import com.stuartbeard.iorek.service.PasswordCheckingService;
import com.stuartbeard.iorek.service.model.CredentialSafety;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static com.stuartbeard.iorek.api.descriptions.APIDescriptions.CREDENTIAL_SAFETY_NAME;

@RestController
@RequestMapping("credential-safety")
@Api(value = CREDENTIAL_SAFETY_NAME,
    produces = MediaType.APPLICATION_JSON_VALUE,
    tags = {"credentials"}
)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CredentialSafetyController implements CredentialSafetyContract {

    private final PasswordCheckingService passwordCheckingService;

    @Override
    @GetMapping(value = "/{credential}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public CredentialSafety getCredentialSafety(@PathVariable("credential") String credential,
                                                @RequestParam(value = "sha1Hash", defaultValue = "true") boolean isSha1Hash) {
        return passwordCheckingService.checkCredentialSafetyInfo(credential, isSha1Hash);
    }
}
