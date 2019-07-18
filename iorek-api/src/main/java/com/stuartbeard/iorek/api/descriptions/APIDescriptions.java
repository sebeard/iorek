package com.stuartbeard.iorek.api.descriptions;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class APIDescriptions {

    public static final String API_200_MESSAGE = "Sent when the API request was successfully processed.";
    public static final String API_429_MESSAGE = "Sent when the too many requests have been made to this API.";
    public static final String API_500_MESSAGE = "Sent when the API encountered an Internal Server problem, perhaps with one of the subsystems.";
    public static final String API_503_MESSAGE = "Sent when the API endpoint timed out during execution.";
    public static final String API_504_MESSAGE = "Sent when the API contacted an external service, which was unavailable.";

    // Credential Safety API
    public static final String GET_CREDENTIAL_SAFETY_DESC = "Used to obtain the safety of using a specific password as a credential.";
    public static final String CREDENTIAL_SAFETY_NAME = "Credential Safety Endpoints";
    public static final String CREDENTIAL_SAFETY_DESC = "Provides insights into the safety of a proposed credential.";

    // Breach Check API
    public static final String GET_IDENTITY_INFO_DESC = "Used to obtain the breach/paste information around an email address.";
    public static final String BREACH_CHECK_NAME = "Breach Information Endpoints";
    public static final String BREACH_CHECK_DESC = "Provides information around data breaches and pastes.";

}
