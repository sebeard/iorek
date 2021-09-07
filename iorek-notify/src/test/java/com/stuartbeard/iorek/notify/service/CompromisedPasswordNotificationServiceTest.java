package com.stuartbeard.iorek.notify.service;

import com.stuartbeard.iorek.service.model.PasswordCheckResult;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CompromisedPasswordNotificationServiceTest {

    private final CompromisedPasswordNotificationService compromisedPasswordNotificationService = new CompromisedPasswordNotificationService() {};

    @Test
    void shouldThrowUnsupportedExceptionByDefaultWhenSendingNotification() {
        UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class,
            () -> compromisedPasswordNotificationService.sendNotification("principal", new PasswordCheckResult()));

        assertThat(exception.getMessage()).isEqualTo("Not implemented");
    }
}
