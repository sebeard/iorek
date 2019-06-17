package com.stuartbeard.iorek.external.hibp.model;

import lombok.Data;
import org.joda.time.DateTime;

import java.util.List;

@Data
public class Breach {

    private String name;
    private String title;
    private String domain;
    private DateTime breachDate;
    private DateTime addedDate;
    private DateTime modifiedDate;
    private int pwnCount;
    private String description;
    private String logoPath;
    private List<String> dataClasses;
    private boolean isVerified;
    private boolean isFabricated;
    private boolean isSensitive;
    private boolean isRetired;
    private boolean isSpamList;
}
