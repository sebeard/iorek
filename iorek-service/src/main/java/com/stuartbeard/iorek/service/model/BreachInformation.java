package com.stuartbeard.iorek.service.model;

import lombok.Data;

import java.util.List;

@Data
public class BreachInformation {

    private String name;
    private String domain;
    //private DateTime breachDate;
    private List<String> informationBreached;
}
