/*
 * MIT License
 *
 * Copyright (c) 2019-2021 Stuart Beard
 */
package com.stuartbeard.iorek.service.recorder;

import com.stuartbeard.iorek.service.model.PasswordCheckResult;

/**
 * Interface with default No-Op implementation. Aimed at providing a simple means of tracking poor password choices
 * and behaviours. Easily integrated with metric, logging or alerting systems where necessary. Primarily put in place
 * to provide monitoring ahead of any user impacting roll out in order to help build a business case or see how bad
 * existing user passwords already are.
 *
 * No implementations, other than Noop, are provided by this library so it is left to the software developer integrating
 * this library to establish
 *
 * @author Stuart Beard
 * @version 1.0.0
 * @since 1.0.0
 */
public interface PasswordCheckRecorder {

    /**
     * Method that passes result of a compromised password check to an external monitoring, logging or metric system.
     *
     * @param requestFlow Which user flow the compromised password check was performed as part of.
     * @param passwordCheckResult The result of the compromised password check.
     */
    default void recordPasswordCheck(String requestFlow, PasswordCheckResult passwordCheckResult) {
        // NOOP: Default we do nothing. This means we can configure an easy zero op if Metric recording gets enabled
        // without the relevant configuration.
    }
}
