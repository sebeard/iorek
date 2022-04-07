Feature: Username is not in Password Check

    Scenario: Service accepts password not containing username
        When I call the service with a username that is not in the password in the payload
        Then the service accepts my request

    Scenario: Service rejects password containing username
        When I call the service with a username that is in the password in the payload
        Then the service rejects my request
