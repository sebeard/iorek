package com.stuartbeard.iorek.test.actions;

import com.stuartbeard.iorek.test.context.ScenarioContext;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static java.lang.String.format;

@RequiredArgsConstructor
public class AccountActions {

    private static final String TOKEN_ENDPOINT = "/oauth2/token";
    private static final String CLIENT_ENDPOINT = "/oauth2/client";
    private static final String CLIENT_ENDPOINT_WITH_CLIENT_ID = CLIENT_ENDPOINT + "/{clientId}";

    private final ScenarioContext scenarioContext;
    private final TestRestTemplate template;

    public void postBasicAuthToken(String clientId, String clientSecret, String grantType, String scope) {
        var httpHeaders = requestHeaderFactory.getStandardRequestHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        var encoder = Base64.getEncoder();
        var authValue = format("%s:%s", clientId, clientSecret);
        var encodedBasicAuthValue = encoder.encodeToString(authValue.getBytes(StandardCharsets.UTF_8));
        httpHeaders.add(HttpHeaders.AUTHORIZATION, format("Basic %s", encodedBasicAuthValue));

        var body = new AccessTokenRequest(grantType, scope);

        var request = new HttpEntity<>(body, httpHeaders);

        exchangeRequest(URI.create(TOKEN_ENDPOINT), HttpMethod.POST, request);

        var accessTokenResponse = scenarioContext.getResponseObjectAs(AccessTokenResponse.class);
        scenarioContext.put(ScenarioContextKeys.ACCESS_TOKEN_KEY, accessTokenResponse.getAccessToken());
    }

    public void postRegisterClient(ClientRegistrationParameters clientRegistrationParameters) {
        var httpHeaders = requestHeaderFactory.getStandardRequestHeaders();

        var accessToken = scenarioContext.get(ScenarioContextKeys.ACCESS_TOKEN_KEY);
        httpHeaders.add(HttpHeaders.AUTHORIZATION, format(BEARER_PATTERN, accessToken));

        var body = new ClientRegistrationRequest(clientRegistrationParameters.getClientName(),
            clientRegistrationParameters.getTokenEndpointAuthMethod(),
            clientRegistrationParameters.getGrantTypes(),
            clientRegistrationParameters.getScope());

        var request = RequestEntity.post(URI.create(CLIENT_ENDPOINT))
            .headers(httpHeaders)
            .body(body);

        exchangeRequest(request);
        updateScenarioContextWithResponseDetails(clientRegistrationParameters.getClientName(), true);
    }

    public void patchUpdateClient(String existingClientId, ClientRegistrationParameters clientRegistrationParameters) {
        var httpHeaders = requestHeaderFactory.getStandardRequestHeaders();

        var accessToken = scenarioContext.get(ScenarioContextKeys.ACCESS_TOKEN_KEY);
        httpHeaders.add(HttpHeaders.AUTHORIZATION, format(BEARER_PATTERN, accessToken));

        var body = new ClientRegistrationRequest(clientRegistrationParameters.getClientName(),
            clientRegistrationParameters.getTokenEndpointAuthMethod(),
            clientRegistrationParameters.getGrantTypes(),
            clientRegistrationParameters.getScope());

        var request = RequestEntity.patch(CLIENT_ENDPOINT_WITH_CLIENT_ID, existingClientId)
            .headers(httpHeaders)
            .body(body);

        exchangeRequest(request);
        updateScenarioContextWithResponseDetails(clientRegistrationParameters.getClientName(), false);
    }

    public void getRegisteredClients() {
        var httpHeaders = requestHeaderFactory.getStandardRequestHeaders();

        var accessToken = scenarioContext.get(ScenarioContextKeys.ACCESS_TOKEN_KEY);
        httpHeaders.add(HttpHeaders.AUTHORIZATION, format(BEARER_PATTERN, accessToken));

        var request = RequestEntity.get(URI.create(CLIENT_ENDPOINT))
            .headers(httpHeaders)
            .build();

        exchangeRequest(request);
    }

    private void exchangeRequest(URI uri, HttpMethod httpMethod, HttpEntity<?> httpEntity) {
        var response = template.exchange(uri, httpMethod, httpEntity, String.class);
        handleResponse(response);
    }

    private void exchangeRequest(RequestEntity<?> requestEntity) {
        var response = template.exchange(requestEntity, String.class);
        handleResponse(response);
    }

    private void handleResponse(ResponseEntity<String> response) {
        scenarioContext.setResponseStatus(response.getStatusCode());
        scenarioContext.setResponseObject(response.getBody());
    }

    private void updateScenarioContextWithResponseDetails(String clientName, boolean storeClientId) {
        if (scenarioContext.getResponseObject() != null) {
            var clientRegistrationResponse = scenarioContext.getResponseObjectAs(ClientRegistrationResponse.class);

            if(storeClientId)
                scenarioContext.put(ScenarioContextKeys.REGISTERED_CLIENT_ID_KEY, clientRegistrationResponse.getClientId());

            scenarioContext.put(ScenarioContextKeys.REGISTERED_CLIENT_SECRET_KEY, clientRegistrationResponse.getClientSecret());
            scenarioContext.put(ScenarioContextKeys.TOKEN_SCOPE, clientRegistrationResponse.getScope());
            scenarioContext.put(ScenarioContextKeys.CLIENT_NAME, clientName);
        }
    }
}
