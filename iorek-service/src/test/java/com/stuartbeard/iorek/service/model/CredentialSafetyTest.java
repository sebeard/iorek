package com.stuartbeard.iorek.service.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.force66.beantester.BeanTester;
import org.junit.jupiter.api.Test;

class CredentialSafetyTest {

    @Test
    void shouldHaveValidGettersAndSetters() {
        new BeanTester().testBean(CredentialSafety.class);
    }

    @Test
    void shouldHaveValidEqualsMethod() {
        EqualsVerifier.simple()
            .forClass(CredentialSafety.class)
            .verify();
    }

}
