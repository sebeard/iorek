package com.stuartbeard.iorek.passwordcheck;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.function.context.FunctionalSpringApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.stuartbeard.iorek")
public class CloudFunction {

    public static void main(String[] args) {
        FunctionalSpringApplication.run(CloudFunction.class, args);
    }
}
