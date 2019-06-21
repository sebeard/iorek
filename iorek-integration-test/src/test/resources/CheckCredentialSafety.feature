Feature: Check Credential Safety

    Background: Given the Iorek API Service is running

    Scenario: Prove Credential Safe for Strong Password
        When I check the safety of my password; mySuperSecretPassword
        Then I expect to be informed that my password is safe
        Then I expect to be informed that my password is allowed
        Then I expect to be informed that my password has been breached 0 times

    Scenario: Prove Credential unsafe and not allowed for Weak Password
        When I check the safety of my password; password
        Then I expect to be informed that my password is unsafe
        Then I expect to be informed that my password is disallowed

    Scenario: Provde Credential Unsafe but allowed for Reasonable Password
        When I check the safety of my password; oreocookie1
        Then I expect to be informed that my password is unsafe
        Then I expect to be informed that my password is allowed
