package com.stuartbeard.iorek.service;

import com.stuartbeard.iorek.service.model.BreachInformation;
import com.stuartbeard.iorek.service.model.PasteInformation;

import javax.annotation.Nonnull;
import java.util.List;

public interface BreachService {

    List<BreachInformation> getBreachInformation(@Nonnull String emailAddress);

    List<PasteInformation> getPasteInformation(@Nonnull String emailAddress);
}
