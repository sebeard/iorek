Feature: Check Credential Safety

    Background:
        Given the Iorek API Service is running

    Scenario: Prove Credential Safe for Strong Password
        When I check the safety of my password; mySuperSecretPassword
        Then I expect to be informed that my password is safe
        And I expect to be informed that my password is allowed
        And I expect to be informed that my password has been breached 0 times

    Scenario: Prove Credential unsafe and not allowed for Weak Password
        When I check the safety of my password; password
        Then I expect to be informed that my password is unsafe
        And I expect to be informed that my password is disallowed

    Scenario: Prove Credential Unsafe but allowed for Reasonable Password
        When I check the safety of my password; oreocookie1
        Then I expect to be informed that my password is unsafe
        And I expect to be informed that my password is allowed

    Scenario: Prove Credential Safe for Strong Hashed Password
        When I check the safety of my hashed password; mySuperSecretPassword
        Then I expect to be informed that my password is safe
        And I expect to be informed that my password is allowed
        And I expect to be informed that my password has been breached 0 times

    Scenario: Prove Credential unsafe and not allowed for Weak Hashed Password
        When I check the safety of my hashed password; password
        Then I expect to be informed that my password is unsafe
        And I expect to be informed that my password is disallowed

    Scenario: Prove Credential Unsafe but allowed for Reasonable Hashed Password
        When I check the safety of my hashed password; oreocookie1
        Then I expect to be informed that my password is unsafe
        And I expect to be informed that my password is allowed
