/*
 * Copyright (c) Firefly.io 2021. All Rights reserved.
 * This software is the confidential and proprietary information of Firefly.io Ltd ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Firefly.io Ltd.
 */
package com.stuartbeard.iorek.notify.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CompromisedPasswordNotificationApplication {

    public static void main(String[] args) {
        SpringApplication.run(CompromisedPasswordNotificationApplication.class, args);
    }
}

