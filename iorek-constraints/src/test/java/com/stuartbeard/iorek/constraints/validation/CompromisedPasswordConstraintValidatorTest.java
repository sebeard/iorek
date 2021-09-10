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

import com.stuartbeard.iorek.constraints.NotKnowinglyCompromised;
import com.stuartbeard.iorek.service.PasswordCheckingService;
import com.stuartbeard.iorek.service.model.PasswordCheckResult;
import com.stuartbeard.iorek.service.model.PasswordRiskLevel;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import javax.validation.ConstraintValidatorContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@MockitoSettings
class CompromisedPasswordConstraintValidatorTest {

    @Mock
    private PasswordCheckingService passwordCheckingService;

    @InjectMocks
    private CompromisedPasswordConstraintValidator compromisedPasswordConstraintValidator;

    @Test
    void shouldFailValidationWithAnnotationCustomisationsForCompromisedPassword() {
        NotKnowinglyCompromised notKnowinglyCompromised = mock(NotKnowinglyCompromised.class);
        when(notKnowinglyCompromised.maxAcceptableRisk()).thenReturn(PasswordRiskLevel.COMPROMISED);
        when(notKnowinglyCompromised.requestFlow()).thenReturn("someRequestFlow");
        when(notKnowinglyCompromised.message()).thenReturn("someMessage");

        ConstraintValidatorContext validatorContext = mock(ConstraintValidatorContext.class);
        when(validatorContext.buildConstraintViolationWithTemplate("someMessage")).thenReturn(mock(ConstraintValidatorContext.ConstraintViolationBuilder.class));
        PasswordCheckResult passwordCheckResult = new PasswordCheckResult()
            .setRiskLevel(PasswordRiskLevel.SEVERELY_COMPROMISED)
            .setCompromisedCount(2);
        when(passwordCheckingService.checkPasswordForKnownCompromise("someRequestFlow", "password", false))
            .thenReturn(passwordCheckResult);

        compromisedPasswordConstraintValidator.initialize(notKnowinglyCompromised);
        boolean isValid = compromisedPasswordConstraintValidator.isValid("password", validatorContext);

        assertThat(isValid).isFalse();
    }

    @Test
    void shouldFailValidationWithDefaultsForCompromisedPassword() {
        ConstraintValidatorContext validatorContext = mock(ConstraintValidatorContext.class);
        when(validatorContext.buildConstraintViolationWithTemplate(anyString())).thenReturn(mock(ConstraintValidatorContext.ConstraintViolationBuilder.class));
        PasswordCheckResult passwordCheckResult = new PasswordCheckResult()
            .setRiskLevel(PasswordRiskLevel.SEVERELY_COMPROMISED)
            .setCompromisedCount(2);
        when(passwordCheckingService.checkPasswordForKnownCompromise("Unknown", "password", false))
            .thenReturn(passwordCheckResult);

        boolean isValid = compromisedPasswordConstraintValidator.isValid("password", validatorContext);

        assertThat(isValid).isFalse();
    }

    @Test
    void shouldPassValidationForUncompromisedPassword() {
        ConstraintValidatorContext validatorContext = mock(ConstraintValidatorContext.class);
        PasswordCheckResult passwordCheckResult = new PasswordCheckResult()
            .setRiskLevel(PasswordRiskLevel.OK)
            .setCompromisedCount(0);
        when(passwordCheckingService.checkPasswordForKnownCompromise("Unknown", "password", false))
            .thenReturn(passwordCheckResult);

        boolean isValid = compromisedPasswordConstraintValidator.isValid("password", validatorContext);

        assertThat(isValid).isTrue();
    }

}
