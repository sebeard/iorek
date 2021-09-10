/*
 * MIT License
 *
 * Copyright (c) 2019-2021 Stuart Beard
 */
package com.stuartbeard.iorek.service.model;

/**
 * Simple enumeration representing the different risk levels associated with a given password after it has had a
 * compromised password check completed. <strong>The risk level is designed to provide guidance only.</strong>.
 * Provides 3 easy enumerations;
 *
 * <ul>
 *     <li>OK - Password is safe to use</li>
 *     <li>COMPROMISED - Password has been compromised but is safe to use based on risk mitigation strategies already in place.</li>
 *     <li>SEVERELY_COMPROMISED - Password has been severely compromised and should not be used</li>
 * </ul>
 *
 * The password risk level is calculated based on user configuration of the libraries.
 *
 * @author Stuart Beard
 * @version 1.0.0
 * @since 1.0.0
 */
public enum PasswordRiskLevel {

    OK,
    COMPROMISED,
    SEVERELY_COMPROMISED
}
