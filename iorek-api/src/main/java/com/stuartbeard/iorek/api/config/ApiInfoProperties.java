package com.stuartbeard.iorek.api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;

import static java.util.Collections.emptyList;

@Data
@Component
@ConfigurationProperties(prefix = "api.info")
public class ApiInfoProperties {

    private String title;
    private String description;
    private String termsOfServiceUrl;
    private Contact contact;
    private String license;
    private String licenseUrl;
    private String version;

    public ApiInfo toApiInfo() {
        return new ApiInfo(title, description, version, termsOfServiceUrl, contact, license, licenseUrl, emptyList());
    }
}
