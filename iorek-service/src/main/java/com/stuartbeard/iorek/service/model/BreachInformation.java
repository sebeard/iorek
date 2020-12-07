package com.stuartbeard.iorek.service.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.List;

@Data
@Accessors(chain = true)
public class BreachInformation {

    private String name;
    private String domain;
    private LocalDate breachDate;
    private List<String> informationBreached;
}
