package com.stuartbeard.iorek.api.model;

import com.stuartbeard.iorek.service.model.BreachInformation;
import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;
import org.meanbean.test.EqualsMethodTester;
import org.meanbean.test.HashCodeMethodTester;

class MessageTest {

    @Test
    void shouldHaveValidGettersAndSetters() {
        new BeanTester().testBean(BreachInformation.class);
    }

    @Test
    void shouldHaveValidEqualsMethod() {
        new EqualsMethodTester().testEqualsMethod(BreachInformation.class);
    }

    @Test
    void shouldHaveValidHashCodeMethod() {
        new HashCodeMethodTester().testHashCodeMethod(BreachInformation.class);
    }
}
