package com.stuartbeard.iorek.sample.api.model;

import com.stuartbeard.iorek.constraints.NotKnowinglyCompromised;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class RegistrationRequest {

    @Email
    @NotBlank
    private String email;

    @Size(min = 8)
    @NotBlank
    @NotKnowinglyCompromised(message = "The password provided for {requestFlow} was too severely compromised and cannot be used.", requestFlow = "registration")
    private String password;
}
