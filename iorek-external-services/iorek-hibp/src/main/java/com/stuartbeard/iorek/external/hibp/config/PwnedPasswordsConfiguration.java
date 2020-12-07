package com.stuartbeard.iorek.external.hibp.config;

import com.stuartbeard.iorek.external.hibp.PwnedPasswordsClient;
import com.stuartbeard.iorek.external.hibp.decoder.CLRFStringDecoder;
import com.stuartbeard.iorek.external.hibp.service.PwnedPasswordsService;
import com.stuartbeard.iorek.service.CompromisedPasswordService;
import feign.Feign;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.UnaryOperator;

import static feign.error.AnnotationErrorDecoder.builderFor;

@Configuration
@ConditionalOnProperty(prefix = "breach.service", value = "name", havingValue = "hibp", matchIfMissing = true)
public class PwnedPasswordsConfiguration {

    @Bean
    UnaryOperator<String> hashFunction() {
        return DigestUtils::sha1Hex;
    }

    @Bean
    CompromisedPasswordService compromisedPasswordService(PwnedPasswordsService pwnedPasswordsService,
                                                          UnaryOperator<String> hashFunction,
                                                          @Value("${breach.service.prefix.length:5}") int prefixLength) {
        return new PwnedPasswordsClient(pwnedPasswordsService, hashFunction, prefixLength);
    }

    @Bean
    PwnedPasswordsService pwnedPasswordsService(@Value("${breach.service.url.pp}") String baseUrl) {
        return Feign.builder()
            .errorDecoder(builderFor(PwnedPasswordsService.class).build())
            .decoder(new CLRFStringDecoder())
            .client(new OkHttpClient())
            .logger(new Slf4jLogger())
            .target(PwnedPasswordsService.class, baseUrl);
    }
}

