package com.stuartbeard.iorek.api.descriptions;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class APIDescriptions {

    public static final String API_200_MESSAGE = "Sent when the API request was successfully processed.";
    public static final String API_429_MESSAGE = "Sent when the requested resource was not found.";
    public static final String API_500_MESSAGE = "Sent when the API encountered an Internal Server problem, perhaps with one of the subsystems.";
    public static final String API_503_MESSAGE = "Sent when the API contacted an external service, which was unavailable.";

    public static final String CREDENTIAL_SAFETY_API = "Used to obtain the safety of using a specific password as a crednetial";

}
