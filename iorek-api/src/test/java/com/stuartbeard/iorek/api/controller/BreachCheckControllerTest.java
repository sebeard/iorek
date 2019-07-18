package com.stuartbeard.iorek.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.stuartbeard.iorek.api.Application;
import com.stuartbeard.iorek.api.model.Message;
import com.stuartbeard.iorek.external.hibp.exception.HIBPBadRequestException;
import com.stuartbeard.iorek.external.hibp.exception.HIBPServiceException;
import com.stuartbeard.iorek.external.hibp.exception.HIBPTooManyRequestsException;
import com.stuartbeard.iorek.service.BreachCheckService;
import com.stuartbeard.iorek.service.model.BreachInformation;
import com.stuartbeard.iorek.service.model.IdentityInformation;
import com.stuartbeard.iorek.service.model.PasteInformation;
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

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;

import static java.util.Collections.singletonList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@WebMvcTest
@ContextConfiguration(classes = Application.class)
class BreachCheckControllerTest {

    private static final String EMAIL_ADDRESS = "someone@test.com";
    private static final String BAD_EMAIL_ADDRESS = "notAnEmail";

    @MockBean
    private BreachCheckService breachCheckService;

    @Autowired
    private MockMvc controller;

    private static <T> String asJson(T object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(new StdDateFormat());
        return mapper.writeValueAsString(object);
    }

    @Test
    void shouldReturnIdentityInformation() throws Exception {
        IdentityInformation identityInformation = new IdentityInformation()
            .setBreaches(singletonList(new BreachInformation()
                .setBreachDate(Date.from(Instant.now()))
                .setDomain("https://www.example.com")
                .setName("Breach Example")
                .setInformationBreached(singletonList("Passwords"))))
            .setPastes(singletonList(new PasteInformation()
                .setTitle("Paste Example")
                .setAdded(Date.from(Instant.now()))))
            .setChecked(Date.from(Instant.now()))
            .setMessage("");
        when(breachCheckService.checkIdentity(EMAIL_ADDRESS)).thenReturn(identityInformation);

        MockHttpServletRequestBuilder request = get("/breach-check?email=" + EMAIL_ADDRESS);

        controller.perform(request)
            .andExpect(status().isOk())
            .andExpect(content().json(asJson(identityInformation)));
    }

    @Test
    void shouldReturnGatewayTimeoutWhenHibpServiceUnavailable() throws Exception {
        when(breachCheckService.checkIdentity(EMAIL_ADDRESS)).thenThrow(new HIBPServiceException("Service Unavailable"));

        MockHttpServletRequestBuilder request = get("/breach-check?email=" + EMAIL_ADDRESS);

        controller.perform(request)
            .andExpect(status().isGatewayTimeout())
            .andExpect(content().json(asJson(new Message("Service Unavailable"))));
    }

    @Test
    void shouldReturnTooManyRequestsWhenHibpServiceIndicatesTooManyRequests() throws Exception {
        when(breachCheckService.checkIdentity(EMAIL_ADDRESS)).thenThrow(new HIBPTooManyRequestsException(new HashMap<>(), "Too Many Requests"));

        MockHttpServletRequestBuilder request = get("/breach-check?email=" + EMAIL_ADDRESS);

        controller.perform(request)
            .andExpect(status().isTooManyRequests())
            .andExpect(content().json(asJson(new Message("Too Many Requests"))));
    }

    @Test
    void shouldReturnBadRequestWhenHibpServiceCalledWithBadEmail() throws Exception {
        when(breachCheckService.checkIdentity(BAD_EMAIL_ADDRESS)).thenThrow(new HIBPBadRequestException("Bad Request"));

        MockHttpServletRequestBuilder request = get("/breach-check?email=" + BAD_EMAIL_ADDRESS);

        controller.perform(request)
            .andExpect(status().isBadRequest())
            .andExpect(content().json(asJson(new Message("Bad Request"))));
    }
}
