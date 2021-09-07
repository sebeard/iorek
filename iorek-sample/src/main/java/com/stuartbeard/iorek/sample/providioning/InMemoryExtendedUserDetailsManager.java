/*
 * MIT License
 *
 * Copyright (c) 2019-2021 Stuart Beard
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
