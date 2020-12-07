package com.stuartbeard.iorek.external.hibp.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.force66.beantester.BeanTester;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class PasteTest {

    @Test
    void shouldHaveValidGettersAndSetters() {
        BeanTester beanTester = new BeanTester();
        beanTester.addTestValues(LocalDate.class, new Object[]{LocalDate.parse("2020-12-03"), LocalDate.parse("2019-01-26")});
        beanTester.testBean(Paste.class);
    }

    @Test
    void shouldHaveValidEqualsMethod() {
        EqualsVerifier.simple()
            .forClass(Paste.class)
            .verify();
    }

}
