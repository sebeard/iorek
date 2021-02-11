package com.stuartbeard.iorek.passwordcheck.model;

import lombok.Data;

@Data
public class PasswordCheckRequest {

    private String password;
    private boolean sha1Hash;
}
