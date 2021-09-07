package com.stuartbeard.iorek.sample.api.model;

import com.stuartbeard.iorek.constraints.NotKnowinglyCompromised;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class ResetPasswordRequest {

    @NotBlank
    private String resetToken;

    @Size(min = 8)
    @NotBlank
    @NotKnowinglyCompromised(requestFlow = "reset-password")
    private String newPassword;
}
