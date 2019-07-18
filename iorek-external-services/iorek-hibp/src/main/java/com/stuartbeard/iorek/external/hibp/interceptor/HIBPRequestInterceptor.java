package com.stuartbeard.iorek.external.hibp.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HIBPRequestInterceptor implements RequestInterceptor {

    private static final String HIBP_API_KEY = "hibp-api-key";

    private final String apiKey;

    public HIBPRequestInterceptor(@Value("${breach.service.apiKey}") String apiKey) {
        this.apiKey = apiKey;
    }


    @Override
    public void apply(RequestTemplate template) {
        template.header(HIBP_API_KEY, apiKey);
    }
}
