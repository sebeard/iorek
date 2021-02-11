package com.stuartbeard.iorek.function.breach;

import com.stuartbeard.iorek.service.BreachCheckService;
import com.stuartbeard.iorek.service.model.IdentityInformation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component(value = "breachFunction")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BreachFunction implements Function<String, IdentityInformation> {

    private final BreachCheckService breachCheckService;

    @Override
    public IdentityInformation apply(final String emailAddress) {
        return breachCheckService.checkIdentity(emailAddress);
    }
}
