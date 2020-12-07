package com.stuartbeard.iorek.api.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.force66.beantester.BeanTester;
import org.junit.jupiter.api.Test;

class ErrorResponseTest {

    @Test
    void shouldHaveValidGettersAndSetters() {
        new BeanTester().testBean(ErrorResponse.class);
    }

    @Test
    void shouldHaveValidEqualsMethod() {
        EqualsVerifier.simple()
            .forClass(ErrorResponse.class)
            .verify();
    }
}
