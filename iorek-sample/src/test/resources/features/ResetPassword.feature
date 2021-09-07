Feature: Request an Access Token with Client Credentials

    Background:
        Given I have an existing account

    Scenario:
        When I attempt to change my password to a known good password
        Then the service accepts my password change

    Scenario:
        When I attempt to change my password to a known bad password
        Then the service accepts my password change

    Scenario:
        When I attempt to change my password to a known terrible password
        Then the service rejects my password change
        And tells me that my password is terrible

    Scenario:
        Given I have forgotten my password
        When I attempt to reset my password to a known good password
        Then the service accepts my password reset

    Scenario:
        Given I have forgotten my password
        When I attempt to reset my password to a known bad password
        Then the service accepts my password reset

    Scenario:
        Given I have forgotten my password
        When I attempt to reset my password to a known terrible password
        Then the service rejects my password reset
        And tells me that my password is terrible
