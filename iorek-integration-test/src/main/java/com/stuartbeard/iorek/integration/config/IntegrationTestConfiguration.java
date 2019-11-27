package com.stuartbeard.iorek.integration.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

@Configuration
@ComponentScan("com.stuartbeard.iorek.integration")
public class IntegrationTestConfiguration {

    public static final int PORT = 8081;
    public static final String HOST = "localhost";

    @Bean
    public TestRestTemplate testRestTemplate(MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter,
                                             @Value("${iorek.api.root.uri:http://localhost:8080}") String rootUri) {
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
            .rootUri(rootUri)
            .additionalMessageConverters(new StringHttpMessageConverter(), mappingJackson2HttpMessageConverter);
        return new TestRestTemplate(restTemplateBuilder);
    }

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(new StdDateFormat());
        return new MappingJackson2HttpMessageConverter(mapper);
    }

    @Bean
    @ConditionalOnProperty(value = "integration.test.environment", havingValue = "", matchIfMissing = true)
    public WireMockServer wireMockServer() {
        WireMockServer wireMockServer = new WireMockServer(options().port(PORT).containerThreads(13));
        wireMockServer.start();
        return wireMockServer;
    }

    @Bean
    @ConditionalOnProperty(value = "integration.test.environment", havingValue = "", matchIfMissing = true)
    public WireMock wireMock() {
        WireMock wireMock = new WireMock(HOST, PORT);
        configureFor(wireMock);
        return wireMock;
    }

}
