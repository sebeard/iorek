package com.stuartbeard.iorek.service.model;

import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;
import org.meanbean.test.EqualsMethodTester;
import org.meanbean.test.HashCodeMethodTester;

class IdentityInformationTest {

    @Test
    void shouldHaveValidGettersAndSetters() {
        new BeanTester().testBean(IdentityInformation.class);
    }

    @Test
    void shouldHaveValidEqualsMethod() {
        new EqualsMethodTester().testEqualsMethod(IdentityInformation.class);
    }

    @Test
    void shouldHaveValidHashCodeMethod() {
        new HashCodeMethodTester().testHashCodeMethod(IdentityInformation.class);
    }

}
