package com.stuartbeard.iorek.external.hibp.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HIBPRequestInterceptor implements RequestInterceptor {

    private static final String HIBP_API_KEY = "hibp-api-key";

    private final String apiKey;

    @Override
    public void apply(RequestTemplate template) {
        template.header(HIBP_API_KEY, apiKey);
    }
}
