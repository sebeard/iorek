package com.stuartbeard.iorek.service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "credential.safety")
@Data
public class CredentialSafetyConfig {

    private boolean preventSevere = true;
    private ThresholdMessage ok;
    private ThresholdMessage severe;
    private ThresholdMessage warning;
}
