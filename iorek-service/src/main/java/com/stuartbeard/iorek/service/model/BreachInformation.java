package com.stuartbeard.iorek.service.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@Data
@Accessors(chain = true)
public class BreachInformation {

    private String name;
    private String domain;
    private Date breachDate;
    private List<String> informationBreached;
}
