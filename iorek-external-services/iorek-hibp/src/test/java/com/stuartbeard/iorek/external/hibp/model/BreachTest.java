package com.stuartbeard.iorek.external.hibp.model;

import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;
import org.meanbean.test.EqualsMethodTester;
import org.meanbean.test.HashCodeMethodTester;

class BreachTest {

    @Test
    void shouldHaveValidGettersAndSetters() {
        new BeanTester().testBean(Breach.class);
    }

    @Test
    void shouldHaveValidEqualsMethod() {
        new EqualsMethodTester().testEqualsMethod(Breach.class);
    }

    @Test
    void shouldHaveValidHashCodeMethod() {
        new HashCodeMethodTester().testHashCodeMethod(Breach.class);
    }
}
