package com.stuartbeard.iorek.integration.test.context;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ContextKey {

    CREDENTIAL_SAFETY("credentialSafety"),
    BREACH_INFORMATION("breachInformation");

    private final String key;
}
