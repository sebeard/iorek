package com.stuartbeard.iorek.sample.providioning;

import org.springframework.security.provisioning.UserDetailsManager;

public interface ExtendedUserDetailsManager extends UserDetailsManager {

    void changeUsername(String newEmail, String currentPwd);
}
