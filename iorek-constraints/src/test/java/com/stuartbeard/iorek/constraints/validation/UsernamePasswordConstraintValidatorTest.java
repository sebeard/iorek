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
package com.stuartbeard.iorek.constraints.validation;

import com.stuartbeard.iorek.constraints.PasswordDoesNotContainUsername;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoSettings;

import javax.validation.ConstraintValidatorContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@MockitoSettings
class UsernamePasswordConstraintValidatorTest {

    private final UsernamePasswordConstraintValidator usernamePasswordConstraintValidator = new UsernamePasswordConstraintValidator();

    @Test
    void shouldFailValidationWithAnnotationCustomisationsForPasswordContainingUsername() {
        PasswordDoesNotContainUsername passwordDoesNotContainUsername = mock(PasswordDoesNotContainUsername.class);
        when(passwordDoesNotContainUsername.passwordField()).thenReturn("password");
        when(passwordDoesNotContainUsername.usernameField()).thenReturn("username");
        when(passwordDoesNotContainUsername.message()).thenReturn("someMessage");
        ConstraintValidatorContext validatorContext = mock(ConstraintValidatorContext.class);
        when(validatorContext.buildConstraintViolationWithTemplate("someMessage")).thenReturn(mock(ConstraintValidatorContext.ConstraintViolationBuilder.class));

        usernamePasswordConstraintValidator.initialize(passwordDoesNotContainUsername);
        RegistrationDTO registrationDTO = new RegistrationDTO();
        registrationDTO.setUsername("dummyUsername");
        registrationDTO.setPassword("passwordFor-dummyUsername");

        boolean isValid = usernamePasswordConstraintValidator.isValid(registrationDTO, validatorContext);

        assertThat(isValid).isFalse();
    }

    @Test
    void shouldFailValidationForPasswordContainingUsername() {
        ConstraintValidatorContext validatorContext = mock(ConstraintValidatorContext.class);
        when(validatorContext.buildConstraintViolationWithTemplate(anyString())).thenReturn(mock(ConstraintValidatorContext.ConstraintViolationBuilder.class));
        RegistrationDTO registrationDTO = new RegistrationDTO();
        registrationDTO.setUsername("dummyUsername");
        registrationDTO.setPassword("passwordFor-dummyUsername");

        boolean isValid = usernamePasswordConstraintValidator.isValid(registrationDTO, validatorContext);

        assertThat(isValid).isFalse();
    }

    @Test
    void shouldPassValidationForPasswordThatDoesNotContainUsername() {
        ConstraintValidatorContext validatorContext = mock(ConstraintValidatorContext.class);
        RegistrationDTO registrationDTO = new RegistrationDTO();
        registrationDTO.setUsername("dummyUsername");
        registrationDTO.setPassword("password");

        boolean isValid = usernamePasswordConstraintValidator.isValid(registrationDTO, validatorContext);

        assertThat(isValid).isTrue();
    }

    @Data
    @PasswordDoesNotContainUsername
    private static class RegistrationDTO {

        private String username;
        private String password;

    }

}
