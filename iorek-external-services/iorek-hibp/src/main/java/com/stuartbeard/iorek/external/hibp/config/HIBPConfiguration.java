package com.stuartbeard.iorek.external.hibp.config;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.stuartbeard.iorek.external.hibp.HIBPClient;
import com.stuartbeard.iorek.external.hibp.interceptor.HIBPRequestInterceptor;
import com.stuartbeard.iorek.external.hibp.mapper.HIBPResponseMapper;
import com.stuartbeard.iorek.external.hibp.service.HIBPService;
import com.stuartbeard.iorek.service.BreachService;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static feign.error.AnnotationErrorDecoder.builderFor;
import static java.util.Collections.singleton;

@Configuration
@ConditionalOnProperty(prefix = "breach.service", value = "name", havingValue = "hibp", matchIfMissing = true)
public class HIBPConfiguration {

    @Bean
    HIBPResponseMapper hibpResponseMapper() {
        return Mappers.getMapper(HIBPResponseMapper.class);
    }

    @Bean
    HIBPRequestInterceptor hibpRequestInterceptor(@Value("${breach.service.apikey}") String apiKey) {
        return new HIBPRequestInterceptor(apiKey);
    }

    @Bean
    BreachService breachService(HIBPService hibpService,
                                HIBPResponseMapper hibpResponseMapper) {
        return new HIBPClient(hibpService, hibpResponseMapper);
    }

    @Bean
    HIBPService hibpService(HIBPRequestInterceptor requestInterceptor, @Value("${breach.service.url.hibp}") String baseUrl) {
        return Feign.builder()
            .requestInterceptor(requestInterceptor)
            .errorDecoder(builderFor(HIBPService.class).build())
            .decoder(new JacksonDecoder(singleton(new JavaTimeModule())))
            .client(new OkHttpClient())
            .logger(new Slf4jLogger())
            .target(HIBPService.class, baseUrl);
    }
}

