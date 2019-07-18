package com.stuartbeard.iorek.integration.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@Data
@Accessors(chain = true)
public class BreachInformationObject {

    private String name;
    private String domain;
    private Date breachDate;
    private List<String> informationBreached;
}
