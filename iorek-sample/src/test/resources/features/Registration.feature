Feature: Request an Access Token with Client Credentials

    Background:
        Given I do not have an existing account

    Scenario:
        When I attempt to register using a known good password
        Then the service accepts my registration

    Scenario:
        When I attempt to register using a known bad password
        Then the service accepts my registration

    Scenario:
        When I attempt to register using a known terrible password
        Then the service rejects my registration
        And tells me that my password is terrible
