package com.stuartbeard.iorek.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.HashSet;

import static java.util.Arrays.asList;
import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket apiDocumentation(ApiInfoProperties apiInfoProperties) {
        return new Docket(DocumentationType.SWAGGER_2)
            .useDefaultResponseMessages(false)
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.stuartbeard.iorek"))
            .paths(regex("/.*"))
            .build()
            .pathMapping("/")
            .apiInfo(apiInfoProperties.toApiInfo())
            .enableUrlTemplating(false)
            .protocols(new HashSet<>(asList("http", "https")));
    }
}
