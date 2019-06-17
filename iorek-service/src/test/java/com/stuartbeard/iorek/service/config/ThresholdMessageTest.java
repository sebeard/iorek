package com.stuartbeard.iorek.service.config;

import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;
import org.meanbean.test.EqualsMethodTester;
import org.meanbean.test.HashCodeMethodTester;

class ThresholdMessageTest {

    @Test
    void shouldHaveValidGettersAndSetters() {
        new BeanTester().testBean(ThresholdMessage.class);
    }

    @Test
    void shouldHaveValidEqualsMethod() {
        new EqualsMethodTester().testEqualsMethod(ThresholdMessage.class);
    }

    @Test
    void shouldHaveValidHashCodeMethod() {
        new HashCodeMethodTester().testHashCodeMethod(ThresholdMessage.class);
    }
}
