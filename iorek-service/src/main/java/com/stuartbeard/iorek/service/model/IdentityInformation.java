package com.stuartbeard.iorek.service.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@Data
@Accessors(chain = true)
public class IdentityInformation {

    private List<BreachInformation> breaches;
    private List<PasteInformation> pastes;
    private String message;
    private Date checked;
}
