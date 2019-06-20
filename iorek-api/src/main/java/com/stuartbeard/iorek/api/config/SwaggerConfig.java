package com.stuartbeard.iorek.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;

import static java.util.Collections.singleton;
import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
public class SwaggerConfig {

    @Bean
    public Docket apiDocumentation() {
        return new Docket(DocumentationType.SWAGGER_2)
            .useDefaultResponseMessages(false)
            .select()
            .apis(RequestHandlerSelectors.any())
            .paths(regex("/.*"))
            .build()
            .pathMapping("/v1")
            //.apiInfo(apiInfo())
            .enableUrlTemplating(true)
            .protocols(singleton("https"));
    }

    @Bean
    public UiConfiguration uiConfiguration() {
        return new UiConfiguration(null);
    }

//    private ApiInfo apiInfo() {
//        return new ApiInfo(
//            apiInfo.getTitle(),
//            apiInfo.getDescription(),
//            apiInfo.getVersion(),
//            apiInfo.getTermsOfService(),
//            new Contact(apiInfo.getContact().getName(), apiInfo.getContact().getEmail(), apiInfo.getContact().getUrl()),
//            apiInfo.getLicenseTitle(),
//            apiInfo.getLicenseUrl()
//        );
//    }
}
