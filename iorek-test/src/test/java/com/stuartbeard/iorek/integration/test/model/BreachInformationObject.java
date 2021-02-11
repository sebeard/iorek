package com.stuartbeard.iorek.integration.test.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.List;

@Data
@Accessors(chain = true)
public class BreachInformationObject {

    private String name;
    private String domain;
    private LocalDate breachDate;
    private List<String> informationBreached;
}
