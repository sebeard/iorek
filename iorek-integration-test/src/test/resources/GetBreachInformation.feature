Feature: Get Breach Information

    Background:
        Given the Iorek API Service is running

    Scenario: Show Breaches and Pastes for known email
        When I check the breach information for my email address; test@test.com
        Then I expect that my email was in 228 data breaches
        And I expect that my email was in 1295 pastes

    Scenario: Show Pastes for known email
        When I check the breach information for my email address; test@test.com
        Then I expect that my email was in 0 data breaches
        And I expect that my email was in 0 pastes

    Scenario: Show Breaches for known email
        When I check the breach information for my email address; someone@test.com
        Then I expect that my email was in 5 data breaches
        And I expect that my email was in 0 pastes

    Scenario: Show no breaches and no pastes for known clean email
        When I check the breach information for my email address; acleantest@test.com
        Then I expect that my email was in 0 data breaches
        And I expect that my email was in 0 pastes
