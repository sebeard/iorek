package com.stuartbeard.iorek.integration.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.springframework.beans.factory.annotation.Value;
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

}
