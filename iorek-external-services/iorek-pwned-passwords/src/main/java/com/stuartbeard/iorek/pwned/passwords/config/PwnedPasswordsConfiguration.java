/*
 * MIT License
 *
 * Copyright (c) 2019-2021 Stuart Beard
 */
package com.stuartbeard.iorek.pwned.passwords.config;

import com.stuartbeard.iorek.pwned.passwords.PwnedPasswordsClient;
import com.stuartbeard.iorek.pwned.passwords.decoder.CLRFStringDecoder;
import com.stuartbeard.iorek.pwned.passwords.service.PwnedPasswordsService;
import com.stuartbeard.iorek.service.external.CompromisedPasswordService;
import feign.Feign;
import feign.error.AnnotationErrorDecoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.UnaryOperator;

/**
 * Spring Configuration class that sets up Spring Beans for the external PwnedPassword service.
 *
 * @author Stuart Beard
 * @version 1.0.0
 * @since 1.0.0
 */
@Configuration
@ConditionalOnProperty(prefix = "compromised.password", name = "service", havingValue = "pwnedpasswords", matchIfMissing = true)
public class PwnedPasswordsConfiguration {

    /**
     * @return A SHA-1 Hash function that takes a string and outputs it's SHA-1 Hash.
     */
    @Bean
    UnaryOperator<String> hashFunction() {
        return DigestUtils::sha1Hex;
    }

    /**
     * Provides the common internal implementation library interface that translates responses from PwnedPasswords
     * into a PasswordCheckResult.
     *
     * @param pwnedPasswordsService the API interface into PwnedPasswords using Feign
     * @param hashFunction a hashing function that takes a raw string input and computes it's hash
     * @param prefixLength The amount of characters to include in the prefix for k-anonymity. Defaults to 5 characters
     *                     if not supplied in properties configuration.
     * @return the PwnedPasswords equivalent implementation if the internal CompromisedPasswordsService Interface.
     */
    @Bean
    CompromisedPasswordService compromisedPasswordService(PwnedPasswordsService pwnedPasswordsService,
                                                          UnaryOperator<String> hashFunction,
                                                          @Value("${compromised.password.pwnedpasswords.prefix.length:5}") int prefixLength) {
        return new PwnedPasswordsClient(pwnedPasswordsService, hashFunction, prefixLength);
    }

    /**
     * Provides the raw API request and response interface for interacting with PwnedPasswords
     *
     * @param baseUrl The base URL for calling the PwnedPasswords API. Defaults to 'https://api.pwnedpasswords.com'
     *                if not supplied in properties configuration.
     * @return an implementation of the API interface into PwnedPasswords using Feign.
     */
    @Bean
    PwnedPasswordsService pwnedPasswordsService(@Value("${compromised.password.pwnedpasswords.url:https://api.pwnedpasswords.com}") String baseUrl) {
        return Feign.builder()
            .errorDecoder(AnnotationErrorDecoder.builderFor(PwnedPasswordsService.class).build())
            .decoder(new CLRFStringDecoder())
            .client(new OkHttpClient())
            .logger(new Slf4jLogger())
            .target(PwnedPasswordsService.class, baseUrl);
    }
}

