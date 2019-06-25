package com.stuartbeard.iorek.external.hibp;

import com.stuartbeard.iorek.external.hibp.mapper.HIBPResponseMapper;
import com.stuartbeard.iorek.external.hibp.service.HIBPService;
import com.stuartbeard.iorek.external.hibp.service.PwnedPasswordsService;
import com.stuartbeard.iorek.service.BreachService;
import com.stuartbeard.iorek.service.model.BreachInformation;
import com.stuartbeard.iorek.service.model.PasteInformation;
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

    private final HIBPService hibpService;
    private final PwnedPasswordsService pwnedPasswordsService;
    private final Function<String, String> hashFunction;
    private final int prefixLength;

    @Autowired
    public HIBPClient(PwnedPasswordsService pwnedPasswordsService,
                      HIBPService hibpService,
                      Function<String, String> hashFunction,
                      @Value("${breach.service.prefix.length:5}") int prefixLength) {
        this.pwnedPasswordsService = pwnedPasswordsService;
        this.hibpService = hibpService;
        this.hashFunction = hashFunction;
        this.prefixLength = prefixLength;
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

    private static Pattern compilePattern(String hashSuffix) {
        String suffixPattern = String.format(COUNT_PATTERN, hashSuffix);
        return Pattern.compile(suffixPattern);
    }

    @Override
    public List<BreachInformation> getBreachInformation(@Nonnull String emailAddress) {
        return HIBPResponseMapper.MAPPER.fromBreaches(hibpService.getBreaches(emailAddress));
    }

    private String getHashPrefix(String passwordHash) {
        return passwordHash.substring(0, prefixLength);
    }

    private String getHashSuffix(String passwordHash) {
        return passwordHash.substring(prefixLength);
    }

    @Override
    public List<PasteInformation> getPasteInformation(@Nonnull String emailAddress) {
        return HIBPResponseMapper.MAPPER.fromPastes(hibpService.getPastes(emailAddress));
    }
}
