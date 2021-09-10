/*
 * MIT License
 *
 * Copyright (c) 2019-2021 Stuart Beard
 */
package com.stuartbeard.iorek.pwned.passwords;

import com.stuartbeard.iorek.service.external.CompromisedPasswordService;
import com.stuartbeard.iorek.pwned.passwords.service.PwnedPasswordsService;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * PwnedPasswords implementation of the {@link CompromisedPasswordService}. Takes in known password or hash
 * representation, calls PwnedPasswords service via RESTful API and returns the appearance count of a given
 * password hash (or zero if not found).
 *
 * @author Stuart Beard
 * @version 1.0.0
 * @since 1.0.0
 * @see CompromisedPasswordService
 */
@RequiredArgsConstructor
public class PwnedPasswordsClient implements CompromisedPasswordService {

    private static final String COUNT_PATTERN = "%s:(?<count>\\d+)";
    private static final String COUNT_GROUP = "count";

    private final PwnedPasswordsService pwnedPasswordsService;
    private final UnaryOperator<String> hashFunction;
    private final int prefixLength;

    /**
     * Calculates the number of times a given password has been seen in known credential breaches. Optionally hashes the
     * input password, splits the hash into a prefix and suffix component, and then calls the PwnedPasswords API using
     * the prefix (for k-anonymity). Searches through returned results for suffix and extracts appearance count if it is found.
     *
     * @param cred A plaintext password or the hash representation of a given password
     * @param isHashRepresentation if the {@link #getAppearanceCount} cred parameter is a Hash Representation (true) or
     *                             Plaintext password (false).
     * @return The number of times the given password has appeared in <strong>known</strong> credentials breaches
     * (if found) otherwise 0 (i.e. not found).
     */
    @Override
    public int getAppearanceCount(@Nonnull String cred, boolean isHashRepresentation) {
        String hash = isHashRepresentation ? cred.toUpperCase() : hashFunction.apply(cred).toUpperCase();
        String hashPrefix = getHashPrefix(hash);
        String hashSuffix = getHashSuffix(hash);
        Pattern countPattern = compilePattern(hashSuffix);

        List<String> matchingHashSuffixes = pwnedPasswordsService.getMatchingSuffixes(hashPrefix);

        return matchingHashSuffixes.stream()
            .filter(countPattern.asPredicate())
            .findAny()
            .map(matchedHash -> {
                Matcher matcher = countPattern.matcher(matchedHash);
                return matcher.find() ? Integer.parseInt(matcher.group(COUNT_GROUP)) : 0;
            }).orElse(0);
    }

    /**
     * Splits a given password hash based on the required k-anonymity prefix length and returns the suffix (i.e.
     * everything after <code>prefixLength</code>).
     *
     * @param passwordHash A hash representation of the password to be checked against PwnedPasswords.
     * @return the last segment of the password hash as defined by the prefix length.
     */
    private String getHashSuffix(String passwordHash) {
        return passwordHash.substring(prefixLength);
    }

    /**
     * Splits a given password hash based on the required k-anonymity prefix length and returns the prefix (i.e.
     * everything before <code>prefixLength</code>).
     *
     * @param passwordHash A hash representation of the password to be checked against PwnedPasswords.
     * @return the first segment of the password hash as defined by the prefix length.
     */
    private String getHashPrefix(String passwordHash) {
        return passwordHash.substring(0, prefixLength);
    }

    /**
     * Provides a compiled pattern
     *
     * @param hashSuffix the last segment of the password hash as defined by the prefix length
     * @return A compiled regular expression to search a given String input for a match.
     */
    private static Pattern compilePattern(String hashSuffix) {
        String suffixPattern = String.format(COUNT_PATTERN, hashSuffix);
        return Pattern.compile(suffixPattern);
    }
}
