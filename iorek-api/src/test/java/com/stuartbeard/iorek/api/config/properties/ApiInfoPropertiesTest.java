package com.stuartbeard.iorek.api.config.properties;

import org.junit.jupiter.api.Test;
import org.meanbean.lang.Factory;
import org.meanbean.test.BeanTester;
import org.meanbean.test.EqualsMethodTester;
import org.meanbean.test.HashCodeMethodTester;
import springfox.documentation.service.Contact;

class ApiInfoPropertiesTest {

    @Test
    void shouldHaveValidGettersAndSetters() {
        BeanTester beanTester = new BeanTester();
        beanTester.getFactoryCollection().addFactory(Contact.class, new ContactFactory());
        beanTester.testBean(ApiInfoProperties.class);
    }

    @Test
    void shouldHaveValidEqualsMethod() {
        EqualsMethodTester equalsMethodTester = new EqualsMethodTester();
        equalsMethodTester.getFactoryCollection().addFactory(Contact.class, new ContactFactory());
        equalsMethodTester.testEqualsMethod(ApiInfoProperties.class);
    }

    @Test
    void shouldHaveValidHashCodeMethod() {
        HashCodeMethodTester hashCodeMethodTester = new HashCodeMethodTester();
        hashCodeMethodTester.getFactoryCollection().addFactory(Contact.class, new ContactFactory());
        hashCodeMethodTester.testHashCodeMethod(ApiInfoProperties.class);
    }


    class ContactFactory implements Factory<Contact> {

        @Override
        public Contact create() {
            return new Contact("name", "url", "email");
        }
    }

}
