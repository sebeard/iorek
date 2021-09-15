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

import com.stuartbeard.iorek.notify.service.CompromisedPasswordNotificationService;
import com.stuartbeard.iorek.service.model.PasswordCheckResult;
import org.springframework.security.core.userdetails.User;

import java.util.HashMap;
import java.util.Map;

public class SimpleCompromisedPasswordNotificationService implements CompromisedPasswordNotificationService {

    private final Map<Object, PasswordCheckResult> passwordCheckResultByPrincipal = new HashMap<>();

    @Override
    public void sendNotification(Object principal, PasswordCheckResult passwordCheckResult) {
        User user = (User) principal;
        passwordCheckResultByPrincipal.put(user.getUsername(), passwordCheckResult);
    }

    public PasswordCheckResult getPasswordCheckResult(String principal) {
        return passwordCheckResultByPrincipal.get(principal);
    }
}
