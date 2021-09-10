Feature:  Out of Band Compromised Password Check

    Scenario:
        When I call the service using a known good password as my credential
        Then the service accepts my request
        And the service does not notify me
        And the password for authentication is recorded as good

    Scenario:
        When I call the service using a known bad password as my credential
        Then the service accepts my request
        And the service notifies out of band me that my password is bad
        And the password for authentication is recorded as bad

    Scenario:
        When I call the service using a known terrible password as my credential
        Then the service accepts my request
        And the service notifies out of band me that my password is terrible
        And the password for authentication is recorded as terrible
