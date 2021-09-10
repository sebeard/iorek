/*
 * Copyright (c) Firefly.io 2021. All Rights reserved.
 * This software is the confidential and proprietary information of Firefly.io Ltd ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Firefly.io Ltd.
 */
package com.stuartbeard.iorek.notify.config;

import com.stuartbeard.iorek.service.PasswordCheckingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.collection.IsMapContaining.hasKey;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {CompromisedPasswordNotificationApplication.class})
class CompromisedPasswordNotificationConfigurationTest {

    @Autowired
    private PasswordCheckingService passwordCheckingService;

    @Test
    void configurationShouldSetupPasswordCheckingService() {
        assertThat(passwordCheckingService).isNotNull();
    }
}
