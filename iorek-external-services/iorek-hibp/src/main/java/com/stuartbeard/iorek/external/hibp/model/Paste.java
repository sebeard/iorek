package com.stuartbeard.iorek.external.hibp.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.joda.time.DateTime;

@Data
@Accessors(chain = true)
public class Paste {

    private String source;
    private String id;
    private String title;
    private DateTime date;
    private int emailCount;
}
