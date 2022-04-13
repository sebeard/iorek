## Contributing to Iorek ##

Contribution to these sets of components and the code stored within this repository is welcomed with open arms, provided
it takes on board the following guidelines.

## Reporting Bugs ##

* Ensure you're running the latest version of Iorek.
* Ensure the bug has not [already been reported](https://github.com/sebeard/iorek/issues).
* If you're unable to find an open issue addressing the problem,
  please [submit a new issue](https://github.com/sebeard/iorek/issues/new).
* Please fill out the appropriate section of the bug report template provided. Please delete any sections not needed in
  the template.

## Reporting Vulnerabilities ##

* If you believe you have found a vulnerability in Iorek itself; please email stuart@stuartbeard.com
* Emails can be PGP encrypted if you wish using keys publicly available
  at [keybase.io/sebeard](https://keybase.io/sebeard)

## Asking Questions ##

* Your question may be answered by taking a look at the documentataion.
* If you still have a question consider opening [a new issue](https://github.com/sebeard/iorek/issues/new).

## Enhancement Requests ##

Suggest changes by [submitting a new issue](https://github.com/sebeard/iorek/issues/new) and begin coding.

## Contributing Code ##

* If you have written a new feature or have fixed a bug please open a new pull request with the patch.
* Ensure the PR description clearly describes the problem and solution. Include any related issue number(s) if
  applicable.
* Please ensure the PR passes the automated checks performed (github-actions, sonarcloud, mutation testing, etc.)
* Please consider adding test cases for any new functionality.

All code within this repository will be peer-reviewed through Pull Requests (PRs). The repository has been restricted on
both the _main_ branch to prevent direct writing, and as such PRs will be required to merge to these branches. All PRs
require a minimum of two approvals.

### Testing Notes ###

Since this is a reusable set of components there are no integration tests for services with the exception of exercising the
integration with PwnedPasswords. These are left for implementation in the respective services where a given component is
used, alongside suitable functional tests that exercise the different scenarios where the component is used.

There are unit tests, and functional tests. We prefer a high level of code coverage, coupled with a relative high level
of mutation coverage. All tests have been written in JUnit while functional tests have been implemented as BDD tests
using the cucumber framework.

The functional tests use the sample module to spin up a very basic service that is capable of exercising the components.
A Sample Controller is provided and spun up with two endpoints each with different access control restrictions. These
endpoints are then called from outside their container with valid or invalid request payloads to ensure our validation,
monitoring, and notification mechanisms are working correctly.

Any tests that are introduced should be clear from their intent and follow the existing style where possible.

## Thank you for your contributions ##

Stuart Beard


