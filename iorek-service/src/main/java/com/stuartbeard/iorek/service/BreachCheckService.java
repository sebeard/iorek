package com.stuartbeard.iorek.service;

import com.stuartbeard.iorek.service.model.BreachInformation;
import com.stuartbeard.iorek.service.model.IdentityInformation;
import com.stuartbeard.iorek.service.model.PasteInformation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.sql.Date;
import java.time.Instant;
import java.util.List;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BreachCheckService {

    private static final String MESSAGE = "Your email address appeared in %d known data breaches, and %d pastes.";

    private BreachService breachService;

    public IdentityInformation checkIdentity(@Nonnull String emailAddress) {
        List<BreachInformation> breachInformation = breachService.getBreachInformation(emailAddress);
        List<PasteInformation> pasteInformation = breachService.getPasteInformation(emailAddress);

        return new IdentityInformation()
            .setBreaches(breachInformation)
            .setPastes(pasteInformation)
            .setChecked(Date.from(Instant.now()))
            .setMessage(String.format(MESSAGE, breachInformation.size(), pasteInformation.size()));
    }
}
