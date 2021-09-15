package com.stuartbeard.iorek.sample.api.exception;

import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@MockitoSettings
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    @Test
    void shouldHandleExceptionAndReturnDefaultMessage() {
        ObjectError objectError = mock(ObjectError.class);
        when(objectError.getDefaultMessage()).thenReturn("Some Default Message");
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getAllErrors()).thenReturn(List.of(objectError));
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        when(exception.getBindingResult()).thenReturn(bindingResult);

        String message = globalExceptionHandler.handleException(exception);

        assertThat(message).isEqualTo("Some Default Message");
    }

    @Test
    void shouldHandleExceptionAndReturn() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getAllErrors()).thenReturn(emptyList());
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        when(exception.getBindingResult()).thenReturn(bindingResult);

        String message = globalExceptionHandler.handleException(exception);

        assertThat(message).isEqualTo("Something Went Wrong");
    }

}
