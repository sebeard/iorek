/*
 * MIT License
 *
 * Copyright (c) 2019-2021 Stuart Beard
 */
package com.stuartbeard.iorek.service.external;

import javax.annotation.Nonnull;

/**
 * Interface that provides common means of retrieving the appearance count of a given password across different
 * implementations.
 *
 * @author Stuart Beard
 * @version 1.0.0
 * @since 1.0.0
 * @see CompromisedPasswordService
 */
public interface CompromisedPasswordService {

    /**
     * Calculates the number of times a given password has been seen in known credential breaches. Implementations will
     * differ between external service provides and/or customised implementations.
     *
     * @param cred A plaintext password or the hash representation of a given password
     * @param isHashRepresentation if the {@link #getAppearanceCount} cred parameter is a Hash Representation (true) or
     *                             Plaintext password (false).
     * @return The number of times the given password has appeared in <strong>known</strong> credentials breaches
     * (if found) otherwise 0 (i.e. not found).
     */
    int getAppearanceCount(@Nonnull String cred, boolean isHashRepresentation);
}
