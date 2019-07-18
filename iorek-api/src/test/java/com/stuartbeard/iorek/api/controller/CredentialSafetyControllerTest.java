package com.stuartbeard.iorek.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.stuartbeard.iorek.api.Application;
import com.stuartbeard.iorek.api.model.Message;
import com.stuartbeard.iorek.external.hibp.exception.PwnedPasswordsServiceException;
import com.stuartbeard.iorek.service.PasswordCheckingService;
import com.stuartbeard.iorek.service.model.CredentialSafety;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@WebMvcTest
@ContextConfiguration(classes = Application.class)
class CredentialSafetyControllerTest {

    private static final String PASSWORD = "password";

    @MockBean
    private PasswordCheckingService passwordCheckingService;

    @Autowired
    private MockMvc controller;

    private static <T> String asJson(T object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());
        return mapper.writeValueAsString(object);
    }

    @Test
    void shouldReturnSafePassword() throws Exception {
        CredentialSafety credentialSafety = new CredentialSafety()
            .setAppearancesInDataSet(0)
            .setPasswordAllowed(true)
            .setSafe(true)
            .setMessage("");
        when(passwordCheckingService.checkCredentialSafetyInfo(PASSWORD, false)).thenReturn(credentialSafety);

        MockHttpServletRequestBuilder request = get("/credential-safety/" + PASSWORD + "?sha1Hash=false");

        controller.perform(request)
            .andExpect(status().isOk())
            .andExpect(content().json(asJson(credentialSafety)));
    }

    @Test
    void shouldReturnGatewayTimeoutWhenPwnedPasswordsServiceUnavailable() throws Exception {
        when(passwordCheckingService.checkCredentialSafetyInfo(PASSWORD, false)).thenThrow(new PwnedPasswordsServiceException("Service Unavailable"));

        MockHttpServletRequestBuilder request = get("/credential-safety/" + PASSWORD + "?sha1Hash=false");

        controller.perform(request)
            .andExpect(status().isGatewayTimeout())
            .andExpect(content().json(asJson(new Message("Service Unavailable"))));
    }
}
