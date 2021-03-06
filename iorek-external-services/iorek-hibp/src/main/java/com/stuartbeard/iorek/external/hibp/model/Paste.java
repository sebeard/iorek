package com.stuartbeard.iorek.external.hibp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Data
@Accessors(chain = true)
public class Paste {

    @JsonProperty("Source")
    private String source;
    @JsonProperty("Id")
    private String id;
    @JsonProperty("Title")
    private String title;
    @JsonProperty("Date")
    private LocalDate date;
    @JsonProperty("EmailCount")
    private Integer emailCount;
}
