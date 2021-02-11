package com.stuartbeard.iorek.passwordcheck;

import com.stuartbeard.iorek.passwordcheck.model.PasswordCheckRequest;
import com.stuartbeard.iorek.service.PasswordCheckingService;
import com.stuartbeard.iorek.service.model.CredentialSafety;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component(value = "passwordCheckingFunction")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PasswordCheckingFunction implements Function<PasswordCheckRequest, CredentialSafety> {

    private final PasswordCheckingService passwordCheckingService;

    @Override
    public CredentialSafety apply(final PasswordCheckRequest passwordCheckRequest) {
        return passwordCheckingService.checkCredentialSafetyInfo(passwordCheckRequest.getPassword(), passwordCheckRequest.isSha1Hash());
    }
}
