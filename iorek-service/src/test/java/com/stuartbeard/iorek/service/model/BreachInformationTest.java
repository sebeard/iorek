package com.stuartbeard.iorek.service.model;

import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;
import org.meanbean.test.EqualsMethodTester;
import org.meanbean.test.HashCodeMethodTester;

class BreachInformationTest {

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
