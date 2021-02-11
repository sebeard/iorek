package com.stuartbeard.iorek.integration.test.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.List;

@Data
@Accessors(chain = true)
public class IdentityInformationObject {

    private List<com.stuartbeard.iorek.integration.test.model.BreachInformationObject> breaches;
    private List<PasteInformationObject> pastes;
    private String message;
    private LocalDate checked;
}
