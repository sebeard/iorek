package com.stuartbeard.iorek.service.config;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.force66.beantester.BeanTester;
import org.junit.jupiter.api.Test;

class ThresholdMessageTest {

    @Test
    void shouldHaveValidGettersAndSetters() {
        new BeanTester().testBean(ThresholdMessage.class);
    }

    @Test
    void shouldHaveValidEqualsMethod() {
        EqualsVerifier.simple()
            .forClass(ThresholdMessage.class)
            .verify();
    }
}
