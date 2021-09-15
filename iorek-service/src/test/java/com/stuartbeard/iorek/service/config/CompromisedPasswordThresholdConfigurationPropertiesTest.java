/*
 * MIT License
 *
 * Copyright (c) 2019-2021 Stuart Beard
 */
package com.stuartbeard.iorek.service.config;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.force66.beantester.BeanTester;
import org.junit.jupiter.api.Test;

class CompromisedPasswordThresholdConfigurationPropertiesTest {

    @Test
    void shouldHaveValidGettersAndSetters() {
        new BeanTester().testBean(CompromisedPasswordThresholdConfigurationProperties.class);
    }

    @Test
    void shouldHaveValidEqualsMethod() {
        EqualsVerifier.simple()
            .forClass(CompromisedPasswordThresholdConfigurationProperties.class)
            .verify();
    }
}
