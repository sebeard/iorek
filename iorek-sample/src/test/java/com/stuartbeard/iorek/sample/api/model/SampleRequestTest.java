package com.stuartbeard.iorek.sample.api.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.force66.beantester.BeanTester;
import org.junit.jupiter.api.Test;

class SampleRequestTest {

    @Test
    void shouldHaveValidGettersAndSetters() {
        new BeanTester().testBean(SampleRequest.class);
    }

    @Test
    void shouldHaveValidEqualsMethod() {
        EqualsVerifier.simple()
            .forClass(SampleRequest.class)
            .verify();
    }

}
