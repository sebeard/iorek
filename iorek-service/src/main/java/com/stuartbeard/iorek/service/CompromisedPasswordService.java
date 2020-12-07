package com.stuartbeard.iorek.service;

import javax.annotation.Nonnull;

public interface CompromisedPasswordService {

    int getAppearanceCount(@Nonnull String password, boolean isSha1Hash);
}
