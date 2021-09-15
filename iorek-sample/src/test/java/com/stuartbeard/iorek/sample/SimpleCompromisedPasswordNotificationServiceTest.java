/*
 * MIT License
 *
 * Copyright (c) 2019-2021 Stuart Beard
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
