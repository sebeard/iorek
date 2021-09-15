package com.stuartbeard.iorek.sample;

import com.stuartbeard.iorek.service.model.PasswordCheckResult;
import com.stuartbeard.iorek.service.model.PasswordRiskLevel;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

class SimpleCompromisedPasswordNotificationServiceTest {

    private final SimpleCompromisedPasswordNotificationService notificationService = new SimpleCompromisedPasswordNotificationService();

    @Test
    void shouldRecordNotificationIntoMap() {
        PasswordCheckResult passwordCheckResult = new PasswordCheckResult();
        passwordCheckResult.setCompromisedCount(12345);
        passwordCheckResult.setRiskLevel(PasswordRiskLevel.COMPROMISED);

        User user = new User("username", "password", emptyList());
        notificationService.sendNotification(user, passwordCheckResult);

        assertThat(notificationService.getPasswordCheckResult(user.getUsername())).isEqualTo(passwordCheckResult);
    }

}
