package com.stuartbeard.iorek.sample.api.controller;

import com.stuartbeard.iorek.sample.api.model.ChangeEmailRequest;
import com.stuartbeard.iorek.sample.api.model.ChangePasswordRequest;
import com.stuartbeard.iorek.sample.api.model.RegistrationRequest;
import com.stuartbeard.iorek.sample.api.model.ResetPasswordRequest;
import com.stuartbeard.iorek.sample.api.model.ResetPasswordTokenRequest;
import com.stuartbeard.iorek.sample.persistence.model.SampleUser;
import com.stuartbeard.iorek.sample.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("account")
public class AccountManagementController {

    private final UserDetailsManager userDetailsManager;
    private final PasswordResetService passwordResetService;
    private final PasswordEncoder passwordEncoder;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping
    public void registerNewUser(@Valid @RequestBody RegistrationRequest registrationRequest) {
        // Deliberately not implemented
        // as sample designed to just show functionality of Compromised Password check
        SampleUser userDetails = new SampleUser();
        userDetails.setUsername(registrationRequest.getEmail());
        userDetails.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        userDetailsManager.createUser(userDetails);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public String huh() {
        return "Hello";
        // Deliberately not implemented
        // as sample designed to just show functionality of Compromised Password check
        //userDetailsManager.createUser(new UserDetails());
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/password")
    public void changeUserPassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        userDetailsManager.changePassword(changePasswordRequest.getOldPassword(), passwordEncoder.encode(changePasswordRequest.getNewPassword()));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/password/reset")
    public void requestPasswordResetToken(@Valid @RequestBody ResetPasswordTokenRequest resetPasswordTokenRequest) {
        if(userDetailsManager.userExists(resetPasswordTokenRequest.getEmail())) {
            passwordResetService.createPasswordResetTokenForUser(resetPasswordTokenRequest.getEmail());
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/password/reset")
    public ResponseEntity<Void> resetUserPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        Optional<String> maybeUsername = passwordResetService.getUsernameByPasswordResetToken(resetPasswordRequest.getResetToken());
        if(maybeUsername.isPresent()) {
            UserDetails user = userDetailsManager.loadUserByUsername(maybeUsername.get());
            SampleUser updatedUser = SampleUser.fromUserDetails(user);
            updatedUser.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
            userDetailsManager.updateUser(updatedUser);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }


    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/email")
    public void changeUserEmail(@AuthenticationPrincipal Principal principal, @Valid @RequestBody ChangeEmailRequest changeEmailRequest) {
        // Deliberately not implemented as sample designed to just show functionality of Compromised Password check as part of AuthenticationSuccessEvent
        //userDetailsManager.changeUsername(changeEmailRequest.getNewEmail(), changeEmailRequest.getPassword());
    }


}
