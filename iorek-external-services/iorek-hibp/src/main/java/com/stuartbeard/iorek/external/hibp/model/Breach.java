package com.stuartbeard.iorek.external.hibp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
public class Breach {

    @JsonProperty("Title")
    private String title;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Domain")
    private String domain;

    @JsonProperty("BreachDate")
    private LocalDate breachDate;

    @JsonProperty("AddedDate")
    private ZonedDateTime addedDate;

    @JsonProperty("ModifiedDate")
    private ZonedDateTime modifiedDate;

    @JsonProperty("PwnCount")
    private Integer pwnCount;

    @JsonProperty("Description")
    private String description;

    @JsonProperty("DataClasses")
    private List<String> dataClasses;

    @JsonProperty("IsVerified")
    private boolean isVerified;

    @JsonProperty("IsSensitive")
    private boolean isSensitive;

    @JsonProperty("IsActive")
    private boolean isActive;

    @JsonProperty("IsRetired")
    private boolean isRetired;

    @JsonProperty("IsFabricated")
    private boolean isFabricated;

    @JsonProperty("IsSpamList")
    private boolean isSpamList;

    @JsonProperty("LogoPath")
    private String logoPath;
}
