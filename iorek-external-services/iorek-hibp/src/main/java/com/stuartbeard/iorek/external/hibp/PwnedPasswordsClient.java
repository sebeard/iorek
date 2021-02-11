package com.stuartbeard.iorek.external.hibp;

import com.stuartbeard.iorek.external.hibp.service.PwnedPasswordsService;
import com.stuartbeard.iorek.service.CompromisedPasswordService;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class PwnedPasswordsClient implements CompromisedPasswordService {

    private static final String COUNT_PATTERN = "%s:(?<count>\\d+)";
    private static final String COUNT_GROUP = "count";

    private final PwnedPasswordsService pwnedPasswordsService;
    private final UnaryOperator<String> hashFunction;
    private final int prefixLength;

    private static Pattern compilePattern(String hashSuffix) {
        String suffixPattern = String.format(COUNT_PATTERN, hashSuffix);
        return Pattern.compile(suffixPattern);
    }

    @Override
    public int getAppearanceCount(@Nonnull String password, boolean isSha1Hash) {
        String hash = isSha1Hash ? password.toUpperCase() : hashFunction.apply(password).toUpperCase();
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

    private String getHashSuffix(String passwordHash) {
        return passwordHash.substring(prefixLength);
    }

    private String getHashPrefix(String passwordHash) {
        return passwordHash.substring(0, prefixLength);
    }
}
