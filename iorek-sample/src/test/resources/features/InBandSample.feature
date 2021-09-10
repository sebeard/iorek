Feature: In Band Compromised Password Check

    Scenario:
        When I call the service with a known good password in the payload
        Then the service accepts my request
        And the password for in-band-sample is recorded as good

    Scenario:
        When I call the service with a known bad password in the payload
        Then the service accepts my request
        And the password for in-band-sample is recorded as bad

    Scenario:
        When I call the service with a known terrible password in the payload
        Then the service rejects my request
        And tells me that my password is terrible
        And the password for in-band-sample is recorded as terrible
