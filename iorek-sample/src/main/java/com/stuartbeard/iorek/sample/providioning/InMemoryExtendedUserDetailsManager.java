package com.stuartbeard.iorek.sample.providioning;

import com.stuartbeard.iorek.sample.persistence.model.SampleUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.util.Assert;

@Slf4j
@RequiredArgsConstructor
public class InMemoryExtendedUserDetailsManager implements ExtendedUserDetailsManager {

    private final UserDetailsManager userDetailsManagerDelegate;
    private final AuthenticationManager authenticationManager;

    @Override
    public void changeUsername(String newUsername, String currentPwd) {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        if (currentUser == null) {
            throw new AccessDeniedException("Can't change username as no Authentication object found in context for current user.");
        } else {
            String currentUsername = currentUser.getName();
            log.debug("Changing username for user '{}' to new username '{}'", currentUsername, newUsername);
            if (this.authenticationManager != null) {
                log.debug("Reauthenticating user '{}' for username change request.", currentUsername);
                this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(currentUsername, currentPwd));
            } else {
                log.debug("No authentication manager set. Password won't be re-checked.");
            }

            SampleUser user = (SampleUser) loadUserByUsername(currentUsername);
            Assert.state(user != null, "Current user doesn't exist in database.");
            user.setUsername(newUsername);
        }
    }

    @Override
    public void createUser(UserDetails userDetails) {
        userDetailsManagerDelegate.createUser(userDetails);
    }

    @Override
    public void updateUser(UserDetails userDetails) {
        userDetailsManagerDelegate.updateUser(userDetails);
    }

    @Override
    public void deleteUser(String s) {
        userDetailsManagerDelegate.deleteUser(s);
    }

    @Override
    public void changePassword(String s, String s1) {
        userDetailsManagerDelegate.changePassword(s, s1);
    }

    @Override
    public boolean userExists(String s) {
        return userDetailsManagerDelegate.userExists(s);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userDetailsManagerDelegate.loadUserByUsername(s);
    }
}
