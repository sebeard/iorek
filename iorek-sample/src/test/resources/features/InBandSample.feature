Feature: In Band Compromised Password Check

    Scenario: Service accepts password that is perceived as good
        When I call the service with a known good password in the payload
        Then the service accepts my request
        And the password for in-band-sample is recorded as good

    Scenario: Service accepts password that is perceived as bad and records it
        When I call the service with a known bad password in the payload
        Then the service accepts my request
        And the password for in-band-sample is recorded as bad

    Scenario: Service rejects password that is perceived as terrible and records it
        When I call the service with a known terrible password in the payload
        Then the service rejects my request
        And tells me that my password is terrible
        And the password for in-band-sample is recorded as terrible
