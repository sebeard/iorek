package com.stuartbeard.iorek.passwordcheck;

import com.stuartbeard.iorek.passwordcheck.model.PasswordCheckRequest;
import com.stuartbeard.iorek.service.model.CredentialSafety;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.function.adapter.aws.SpringBootRequestHandler;

@Slf4j
public class PasswordCheckingFunctionHandler extends SpringBootRequestHandler<PasswordCheckRequest, CredentialSafety> {
}
