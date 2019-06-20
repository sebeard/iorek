package com.stuartbeard.iorek.service.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CredentialSafety {

    private boolean safe;
    private boolean passwordAllowed;
    private int appearancesInDataSet;
    private String message;
}
