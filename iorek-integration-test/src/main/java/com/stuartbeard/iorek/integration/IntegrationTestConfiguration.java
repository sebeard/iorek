package com.stuartbeard.iorek.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
@ComponentScan("com.stuartbeard.iorek.integration")
public class IntegrationTestConfiguration {

    @Bean
    public TestRestTemplate testRestTemplate(MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter) {
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
            .rootUri("http:/192.168.64.2::8080")
            .additionalMessageConverters(new StringHttpMessageConverter(), mappingJackson2HttpMessageConverter);
        return new TestRestTemplate(restTemplateBuilder);
    }

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        return new MappingJackson2HttpMessageConverter(new ObjectMapper());
    }

}
