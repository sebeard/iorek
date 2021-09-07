/*
 * MIT License
 *
 * Copyright (c) 2019-2021 Stuart Beard
 */
package com.stuartbeard.iorek.service.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.force66.beantester.BeanTester;
import org.junit.jupiter.api.Test;

class PasswordCheckResultTest {

    @Test
    void shouldHaveValidGettersAndSetters() {
        new BeanTester().testBean(PasswordCheckResult.class);
    }

    @Test
    void shouldHaveValidEqualsMethod() {
        EqualsVerifier.simple()
            .forClass(PasswordCheckResult.class)
            .verify();
    }

}
