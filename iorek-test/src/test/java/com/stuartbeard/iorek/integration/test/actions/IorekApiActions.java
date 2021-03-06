package com.stuartbeard.iorek.integration.test.actions;

import com.stuartbeard.iorek.integration.test.model.CredentialSafetyObject;
import com.stuartbeard.iorek.integration.test.model.IdentityInformationObject;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import static org.assertj.core.api.Assertions.assertThat;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
class IorekApiActions {

    private static final String PING = "http://localhost:8080/actuator/health";
    private static final String GET_CREDENTIAL_SAFETY = "http://localhost:8080/credential-safety/{credential}?sha1Hash={sha1Hashed}";
    private static final String GET_IDENTITY_INFO = "http://localhost:8080/breach-check?email={email}";
    private final TestRestTemplate testRestTemplate;

    boolean ping() {
        ResponseEntity<Void> responseEntity = testRestTemplate.getForEntity(PING, Void.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        return true;
    }

    CredentialSafetyObject getCredentialSafety(String password, boolean sha1Hashed) {
        ResponseEntity<CredentialSafetyObject> responseEntity = testRestTemplate.getForEntity(GET_CREDENTIAL_SAFETY, CredentialSafetyObject.class, password, sha1Hashed);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        return responseEntity.getBody();
    }

    IdentityInformationObject getIdentityInfo(String email) {
        ResponseEntity<IdentityInformationObject> responseEntity = testRestTemplate.getForEntity(GET_IDENTITY_INFO, IdentityInformationObject.class, email);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        return responseEntity.getBody();
    }
}
