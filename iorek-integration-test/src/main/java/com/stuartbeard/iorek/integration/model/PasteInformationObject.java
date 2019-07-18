package com.stuartbeard.iorek.integration.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class PasteInformationObject {

    private String title;
    private Date added;
}
