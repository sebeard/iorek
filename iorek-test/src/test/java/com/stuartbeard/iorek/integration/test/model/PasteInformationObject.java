package com.stuartbeard.iorek.integration.test.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Data
@Accessors(chain = true)
public class PasteInformationObject {

    private String title;
    private LocalDate added;
}
