package com.stuartbeard.iorek.function.breach;

import com.stuartbeard.iorek.external.hibp.decoder.CLRFStringDecoder;
import com.stuartbeard.iorek.external.hibp.interceptor.HIBPRequestInterceptor;
import com.stuartbeard.iorek.external.hibp.service.HIBPService;
import com.stuartbeard.iorek.external.hibp.service.PwnedPasswordsService;
import feign.Feign;
import feign.error.AnnotationErrorDecoder;
import feign.jackson.JacksonDecoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
@ConditionalOnProperty(prefix = "breach.service", value = "name", havingValue = "hibp", matchIfMissing = true)
@ComponentScan("com.stuartbeard.iorek.external.hibp")
public class HIBPConfig {

    @Bean
    Function<String, String> hashFunction() {
        return DigestUtils::sha1Hex;
    }

    @Bean
    PwnedPasswordsService pwnedPasswordsService(@Value("${breach.service.url.pp}") String baseUrl) {
        return Feign.builder()
            .errorDecoder(AnnotationErrorDecoder.builderFor(PwnedPasswordsService.class).build())
            .decoder(new CLRFStringDecoder())
            .client(new OkHttpClient())
            .logger(new Slf4jLogger())
            .target(PwnedPasswordsService.class, baseUrl);
    }

    @Bean
    HIBPService hibpService(HIBPRequestInterceptor requestInterceptor, @Value("${breach.service.url.hibp}") String baseUrl) {
        return Feign.builder()
            .requestInterceptor(requestInterceptor)
            .errorDecoder(AnnotationErrorDecoder.builderFor(HIBPService.class).build())
            .decoder(new JacksonDecoder())
            .client(new OkHttpClient())
            .logger(new Slf4jLogger())
            .target(HIBPService.class, baseUrl);
    }
}
