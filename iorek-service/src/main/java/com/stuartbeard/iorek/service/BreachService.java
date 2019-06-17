package com.stuartbeard.iorek.service;

import javax.annotation.Nonnull;

public interface BreachService {

    int getAppearanceCount(@Nonnull String passwordHash);

    //List<BreachInformation> getBreachInformation(@Nonnull String emailAddress);

    //List<PasteInformation> getPasteInformation(@Nonnull String emailAddress);
}
