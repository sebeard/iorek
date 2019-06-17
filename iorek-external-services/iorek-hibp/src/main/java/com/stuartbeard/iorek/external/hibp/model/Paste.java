package com.stuartbeard.iorek.external.hibp.model;

import lombok.Data;
import org.joda.time.DateTime;

@Data
public class Paste {

    private String source;
    private String id;
    private String title;
    private DateTime date;
    private int emailCount;
}
