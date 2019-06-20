package com.stuartbeard.iorek.service.model;

import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;
import org.meanbean.test.EqualsMethodTester;
import org.meanbean.test.HashCodeMethodTester;

class PasteInformationTest {

    @Test
    void shouldHaveValidGettersAndSetters() {
        new BeanTester().testBean(PasteInformation.class);
    }

    @Test
    void shouldHaveValidEqualsMethod() {
        new EqualsMethodTester().testEqualsMethod(PasteInformation.class);
    }

    @Test
    void shouldHaveValidHashCodeMethod() {
        new HashCodeMethodTester().testHashCodeMethod(PasteInformation.class);
    }

}
