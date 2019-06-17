package com.stuartbeard.iorek.external.exception;

import com.stuartbeard.iorek.external.hibp.exception.HIBPServiceException;
import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;

class HIBPServiceExceptionTest {

    @Test
    void shouldHaveValidGetters() {
        new BeanTester().testBean(HIBPServiceException.class);
    }
}
