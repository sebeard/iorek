package com.stuartbeard.iorek.integration;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class IorekApiActions {

    private static final String PING = "";
    private static final String GET_CREDENTIAL_SAFETY = "http://192.168.64.2:8080/credential-safety/{credential}?sha1Hash={sha1Hashed}";
    private final TestRestTemplate testRestTemplate;

    public boolean ping() {
        ResponseEntity<Void> responseEntity = testRestTemplate.getForEntity(PING, Void.class);
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        return true;
    }

    public CredentialSafetyObject getCredentialSafety(String password, boolean sha1Hashed) {
        ResponseEntity<CredentialSafetyObject> responseEntity = testRestTemplate.getForEntity(GET_CREDENTIAL_SAFETY, CredentialSafetyObject.class, password, sha1Hashed);
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        return responseEntity.getBody();
    }
}
