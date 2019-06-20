package com.stuartbeard.iorek.service.model;

import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;
import org.meanbean.test.EqualsMethodTester;
import org.meanbean.test.HashCodeMethodTester;

class CredentialSafetyTest {

    @Test
    void shouldHaveValidGettersAndSetters() {
        new BeanTester().testBean(CredentialSafety.class);
    }

    @Test
    void shouldHaveValidEqualsMethod() {
        new EqualsMethodTester().testEqualsMethod(CredentialSafety.class);
    }

    @Test
    void shouldHaveValidHashCodeMethod() {
        new HashCodeMethodTester().testHashCodeMethod(CredentialSafety.class);
    }

}
