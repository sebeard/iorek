package com.stuartbeard.iorek.sample.api.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class ChangeEmailRequest {

    @NotBlank
    @Email
    private String newEmail;

    @NotBlank
    private String password;
}
