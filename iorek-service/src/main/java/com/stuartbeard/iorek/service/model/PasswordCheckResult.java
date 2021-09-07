/*
 * MIT License
 *
 * Copyright (c) 2019-2021 Stuart Beard
 */
package com.stuartbeard.iorek.service.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Simple POJO containing the result of a compromised password check. Encapsulates the raw count of appearances a given
 * password in <strong>known</strong> credential breaches, and a simple enumeration indicating the risk level
 * (calculated based on user configuration of the libraries in use).
 *
 * @author Stuart Beard
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
@Accessors(chain = true)
public class PasswordCheckResult {

    private int compromisedCount;
    private PasswordRiskLevel riskLevel;
}
