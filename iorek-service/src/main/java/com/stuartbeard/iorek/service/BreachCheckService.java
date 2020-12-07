package com.stuartbeard.iorek.service;

import com.stuartbeard.iorek.service.model.BreachInformation;
import com.stuartbeard.iorek.service.model.IdentityInformation;
import com.stuartbeard.iorek.service.model.PasteInformation;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nonnull;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
public class BreachCheckService {

    private static final String MESSAGE = "Your email address appeared in %d known data breaches, and %d pastes.";

    private final BreachService breachService;

    public IdentityInformation checkIdentity(@Nonnull String emailAddress) {
        List<BreachInformation> breachInformation = breachService.getBreachInformation(emailAddress);
        List<PasteInformation> pasteInformation = breachService.getPasteInformation(emailAddress);

        return new IdentityInformation()
            .setBreaches(breachInformation)
            .setPastes(pasteInformation)
            .setChecked(LocalDate.now())
            .setMessage(String.format(MESSAGE, breachInformation.size(), pasteInformation.size()));
    }
}
