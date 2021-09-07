/*
 * MIT License
 *
 * Copyright (c) 2019-2021 Stuart Beard
 */
package com.stuartbeard.iorek.service.config;

import lombok.Data;

/**
 * Simple POJO containing configuration properties for warning and severe thresholds.
 *
 * @author Stuart Beard
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
public class CompromisedPasswordThresholdConfigurationProperties {

    private int warning = 1;
    private int severe = 1;
}
