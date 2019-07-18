package com.stuartbeard.iorek.service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "credential.safety")
@Data
public class CredentialSafetyConfig {

    private boolean preventSevere = true;
    private ThresholdMessage ok = new ThresholdMessage().setMessage("credential.safety.ok").setThreshold(0);
    private ThresholdMessage warning = new ThresholdMessage().setMessage("credential.safety.warning").setThreshold(0);
    private ThresholdMessage severe = new ThresholdMessage().setMessage("credential.safety.severe").setThreshold(0);
}
