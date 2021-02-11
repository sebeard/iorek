package com.stuartbeard.iorek.service.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.List;

import static java.util.Collections.emptyList;

@Data
@Accessors(chain = true)
public class IdentityInformation {

    private List<BreachInformation> breaches = emptyList();
    private List<PasteInformation> pastes = emptyList();
    private String message;
    private LocalDate checked;
}
