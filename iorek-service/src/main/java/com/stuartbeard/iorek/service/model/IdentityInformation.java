package com.stuartbeard.iorek.service.model;

import lombok.Data;

import java.util.List;

@Data
public class IdentityInformation {

    List<BreachInformation> breaches;
    List<PasteInformation> pastes;
    String message;
}
