package com.stuartbeard.iorek.integration.actions;

import com.stuartbeard.iorek.integration.model.CredentialSafetyObject;
import com.stuartbeard.iorek.integration.model.IdentityInformationObject;
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

    private static final String PING = "http://localhost:8080/actuator/health";
    private static final String GET_CREDENTIAL_SAFETY = "http://localhost:8080/credential-safety/{credential}?sha1Hash={sha1Hashed}";
    private static final String GET_IDENTITY_INFO = "http://localhost:8080/breach-check?email={email}";
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

    public IdentityInformationObject getIdentityInfo(String email) {
        ResponseEntity<IdentityInformationObject> responseEntity = testRestTemplate.getForEntity(GET_IDENTITY_INFO, IdentityInformationObject.class, email);
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        return responseEntity.getBody();
    }
}
