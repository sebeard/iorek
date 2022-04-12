package com.stuartbeard.iorek.constraints.config;

import lombok.Data;

/**
 * Simple POJO containing configuration properties for monitoring.
 *
 * @author Stuart Beard
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
public class CompromisedPasswordMonitoringProperties {

    private boolean only = false;
}
