package com.stuartbeard.iorek.external.hibp;

import com.stuartbeard.iorek.external.hibp.service.PwnedPasswordsService;
import com.stuartbeard.iorek.service.BreachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class HIBPClient implements BreachService {

    private static final String COUNT_PATTERN = "%s:(?<count>\\d+)";
    private static final String COUNT_GROUP = "count";

    private PwnedPasswordsService pwnedPasswordsService;
    private Function<String, String> hashFunction;
    private int prefixLength;

    @Autowired
    public HIBPClient(PwnedPasswordsService pwnedPasswordsService,
                      Function<String, String> hashFunction,
                      @Value("${breach.service.prefix.length:5}") int prefixLength) {
        this.pwnedPasswordsService = pwnedPasswordsService;
        this.hashFunction = hashFunction;
        this.prefixLength = prefixLength;
    }

    private static Pattern compilePattern(String hashSuffix) {
        String suffixPattern = String.format(COUNT_PATTERN, hashSuffix);
        return Pattern.compile(suffixPattern);
    }

    @Override
    public int getAppearanceCount(@Nonnull String password) {
        String hash = hashFunction.apply(password).toUpperCase();
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

    private String getHashPrefix(String passwordHash) {
        return passwordHash.substring(0, prefixLength);
    }

    private String getHashSuffix(String passwordHash) {
        return passwordHash.substring(prefixLength);
    }
}
