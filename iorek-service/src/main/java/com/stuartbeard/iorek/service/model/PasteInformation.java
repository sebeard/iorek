package com.stuartbeard.iorek.service.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Data
@Accessors(chain = true)
public class PasteInformation {

    private String title;
    private LocalDate added;
}
