/*
 * MIT License
 *
 * Copyright (c) 2019-2021 Stuart Beard
 */
package com.stuartbeard.iorek.notify.service;

import com.stuartbeard.iorek.service.model.PasswordCheckResult;

/**
 * Interface that accepts an identifier of an individual based on the principal and a password check result.
 * Designed to act on the password check result by notifying the principal of the result. Alternatively can be used to
 * alert system administrator of other systems of a password check result associated with a principal. Or both.
 *
 * @author Stuart Beard
 * @version 1.0.0
 * @since 1.0.0
 */
public interface CompromisedPasswordNotificationService {

    /**
     * Interface method that enables a password check result be acted on in association with the user or entity
     * (principal) that triggered the check.
     *
     * @param principal An identifier object of the user associated with the password check result
     * @param passwordCheckResult The result of a call to an external password checking service
     */
    default void sendNotification(Object principal, PasswordCheckResult passwordCheckResult) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
