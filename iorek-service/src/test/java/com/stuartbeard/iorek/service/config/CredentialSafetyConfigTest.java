package com.stuartbeard.iorek.service.config;

import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;
import org.meanbean.test.EqualsMethodTester;
import org.meanbean.test.HashCodeMethodTester;

class CredentialSafetyConfigTest {

    @Test
    void shouldHaveValidGettersAndSetters() {
        new BeanTester().testBean(CredentialSafetyConfig.class);
    }

    @Test
    void shouldHaveValidEqualsMethod() {
        new EqualsMethodTester().testEqualsMethod(CredentialSafetyConfig.class);
    }

    @Test
    void shouldHaveValidHashCodeMethod() {
        new HashCodeMethodTester().testHashCodeMethod(CredentialSafetyConfig.class);
    }
}
