package com.stuartbeard.iorek.service.config;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.force66.beantester.BeanTester;
import org.junit.jupiter.api.Test;

class CredentialSafetyConfigTest {

    @Test
    void shouldHaveValidGettersAndSetters() {
        new BeanTester().testBean(CredentialSafetyConfig.class);
    }

    @Test
    void shouldHaveValidEqualsMethod() {
        EqualsVerifier.simple()
            .forClass(CredentialSafetyConfig.class)
            .verify();
    }
}
