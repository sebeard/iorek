package com.stuartbeard.iorek.external.hibp.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.force66.beantester.BeanTester;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZonedDateTime;

class BreachTest {

    @Test
    void shouldHaveValidGettersAndSetters() {
        BeanTester beanTester = new BeanTester();
        beanTester.addTestValues(LocalDate.class, new Object[]{LocalDate.parse("2020-12-03"), LocalDate.parse("2019-01-26")});
        beanTester.addTestValues(ZonedDateTime.class, new Object[]{ZonedDateTime.parse("2020-12-03T09:00:00Z"), ZonedDateTime.parse("2019-01-26T23:15:54Z")});
        beanTester.testBean(Breach.class);
    }

    @Test
    void shouldHaveValidEqualsMethod() {
        EqualsVerifier.simple()
            .forClass(Breach.class)
            .verify();
    }

}
