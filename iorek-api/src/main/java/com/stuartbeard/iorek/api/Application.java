package com.stuartbeard.iorek.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableConfigurationProperties
@ComponentScan("com.stuartbeard")
public class Application {

    public static void main(String... args) {
        SpringApplication.run(Application.class, args);
    }
}
