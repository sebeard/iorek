package com.stuartbeard.iorek.integration.model;

import lombok.Data;

@Data
public class CredentialSafetyObject {

    private boolean safe;
    private boolean passwordAllowed;
    private int appearancesInDataSet;
    private String message;
}
