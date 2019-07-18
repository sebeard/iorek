package com.stuartbeard.iorek.integration.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@Data
@Accessors(chain = true)
public class IdentityInformationObject {

    private List<BreachInformationObject> breaches;
    private List<PasteInformationObject> pastes;
    private String message;
    private Date checked;
}
