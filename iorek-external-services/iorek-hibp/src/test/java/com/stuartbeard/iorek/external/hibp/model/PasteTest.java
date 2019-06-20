package com.stuartbeard.iorek.external.hibp.model;

import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;
import org.meanbean.test.EqualsMethodTester;
import org.meanbean.test.HashCodeMethodTester;

class PasteTest {

    @Test
    void shouldHaveValidGettersAndSetters() {
        new BeanTester().testBean(Paste.class);
    }

    @Test
    void shouldHaveValidEqualsMethod() {
        new EqualsMethodTester().testEqualsMethod(Paste.class);
    }

    @Test
    void shouldHaveValidHashCodeMethod() {
        new HashCodeMethodTester().testHashCodeMethod(Paste.class);
    }
}
