package com.stuartbeard.iorek.sample.service;

import com.stuartbeard.iorek.sample.persistence.model.SampleUser;
import org.springframework.security.crypto.keygen.Base64StringKeyGenerator;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PasswordResetService {

    private static final StringKeyGenerator TOKEN_GENERATOR = new Base64StringKeyGenerator(Base64.getUrlEncoder().withoutPadding(), 48);
    private final Map<String, String> passwordResetTokenUserNameMap = new ConcurrentHashMap<>();

    public void createPasswordResetTokenForUser(String username) {
        passwordResetTokenUserNameMap.putIfAbsent(TOKEN_GENERATOR.generateKey(), username);
    }

    public Optional<String> getUsernameByPasswordResetToken(final String token) {
        return Optional.ofNullable(passwordResetTokenUserNameMap.get(token));
    }
}
