package com.stuartbeard.iorek.sample.api.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class ResetPasswordTokenRequest {

    @NotBlank
    @Email
    private String email;
}
