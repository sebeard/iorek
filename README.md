# Iorek

![Maven Central](https://img.shields.io/maven-central/v/com.stuartbeard.iorek/iorek-constraints) ![Libraries.io dependency status for GitHub repo](https://img.shields.io/librariesio/github/sebeard/iorek) [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=sebeard_iorek&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=sebeard_iorek) [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=sebeard_iorek&metric=coverage)](https://sonarcloud.io/summary/new_code?id=sebeard_iorek) ![GitHub](https://img.shields.io/github/license/sebeard/iorek) ![GitHub Sponsors](https://img.shields.io/github/sponsors/sebeard)

Iorek is a small set of Spring Boot libraries designed to provide compromised password checking functionality through
fairly minimal configuration and effort.

Iorek's core capability is to check the perceived healthiness of a password; specifically has it been seen in a known leaked
credential collection, and if so how many times. Using this information and configuration properties provided it then
provides a simple perceived Risk Level response, as well as the raw count.

## Iorek Constraints

Iorek Constraints is designed for in-band notification of a compromised password. It provides a simple Bean Validation
annotation along with configuration that enables a password input as part of an API Request to be checked for
known compromise.

### Installation

To include the dependency in your project put the following in the relevant build script.

#### Maven

```xml
<dependency>
    <groupId>com.stuartbeard.iorek</groupId>
    <artifactId>iorek-constraints</artifactId>
    <version>1.0.0</version>
</dependency>
```

#### Gradle

```groovy
implementation group: 'com.stuartbeard.iorek', name: 'iorek-constraints', version: '1.0.0'
```

#### Setup
 1. Import the `CompromisedPasswordConstraintConfiguration` class using `@Import` within one of your own Spring Boot
Application's Configuration classes.
 2. Annotate the fields within your API Model classes that you wish to check for known password compromise with
`@NotKnowinglyCompromised`. Customise the annotation defaults as necessary

## Iorek Notify

Iorek Notify is designed for out of band notification of a compromised password. It provides an event listener along
with configuration that enables a successful authentication attempt (with a username and password) to be checked for
known compromise.

### Installation

To include both dependencies in your project put the following in the relevant build script. If you only wish to use
one of the libraries copy and paste the relevant dependency into the same build script.

#### Maven

```xml
<dependency>
    <groupId>com.stuartbeard.iorek</groupId>
    <artifactId>iorek-notify</artifactId>
    <version>1.0.0</version>
</dependency>
```

#### Gradle

```groovy
implementation group: 'com.stuartbeard.iorek', name: 'iorek-notify', version: '1.0.0'
```

#### Setup
1. Import the `CompromisedPasswordNotificationConfiguration` class using `@Import` within one of your own Spring Boot
   Application's Configuration classes.
2. Within your extension of the Spring Security `WebSecurityConfigurerAdapter` class ensure that the credentials are
not erased. **This is important** otherwise out of band checks on passwords like login or change email cannot be
monitored. The credentials are erased i nthe event listener after being checked for known compromise;
```java
@Override
protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
    auth.eraseCredentials(false);
}
```

## Configuration

Using this client integration should require minimum configuration. Most configuration values are defaulted, and the
current (and only integration) is set to PwnedPassword.

For a reasonable setup please review the code in `iorek-sample` which is designed as a very basic Spring Boot
application that exercises these libraries under test.

### Recording Metrics

By default a zero operation Metric Recorder is provided. This is defined as a conditional spring bean which means that
adding your own implementation is a simple as creating a new class that implements the `PasswordCheckRecorder` interface
and declaring it either as a Spring Bean using `@Component` and scanning for it or putting into a Configuration class
file annotated with `@Configuration`. This is purely optional and by default metrics will not be recorded. Be aware
that doing so and setting the option for monitoring only mode will mean that the libraries will call out to the external
password checking service but do absolutely nothing as a result.

The idea here is to bring your own metrics recording mechanism whatever that maybe - Elastic,
Prometheus, Micrometer, Cloudwatch etc. This library does not want to make assumptions about individual configurations
or code setup but may add support in a targeted way at a later point.

### Notifier

**NOTE** If you are using `iorek-notify` then you WILL NEED this configuration. If you're not using `iorek-notify` then
you can safely ignore this configuration.

By default an unimplemented `CompromisedPasswordNotificationService` is provided. This is defined as a conditional
spring bean which means that adding your own implementation is a simple as creating a new class that implements the
`CompromisedPasswordNotificationService` interface and declaring it either as a Spring Bean using `@Service` and
scanning for it or putting into a Configuration class file annotated with `@Configuration`. This is not optional if
monitoring only mode is disabled since it will result in Unsupported Operation Exceptions being thrown at runtime.

The idea here is to bring your own notification mechanism whatever that maybe - SES, JavaMail, SNS, etc. This library
does not want to make assumptions about individual configurations or code setup but may add support in a targeted way
at a later point.

### Properties

The following properties are available to be set in either your `application.yml` file or `application.properties` file.
A full example configuration with defaults is provided at the bottom of this section

#### Service
- `compromised.password.service` determines which integration to use. Defaults automatically to "pwnedpassword" if not supplied.

#### Pwned Passwords Configuration
- `compromised.password.pwnedpasswords.url` The API base URL to the Pwned Passwords API. At the time of writing this is
`https://api.pwnedpasswords.com` which is the default.
- `compromised.password.pwnedpasswords.prefix.length` This is the required prefix length of the SHA-1 Hash provided to
the Pwned Passwords API for k-anonymity. Currently, the requirement is for the first 5 characters of the hash, so this
defaults to `5` if not provided.

#### Threshold Configuration

This sets up the thresholds for how safe a password is considered.

- Severe takes precedence over the warning threshold
  - Setting the warning threshold higher than the severe threshold will have no affect.
- Setting the warning threshold equal to the severe threshold will turn off warning messaging.
- Setting the severe threshold equal to 0 will mark all credentials as unsafe.

Out of the box configuration (defaults detailed below) means that any password that has been seen in a known credential
breach is considered as severely compromised.

**IMPORTANT NOTE 1** A password that does not appear in a data set is NOT necessarily safe, and does not mean it has NOT
been compromised. It is merely a confidence indicator that it is not known whether or not a password has been compromised.

**IMPORTANT NOTE 2** It is up to any consumer of these libraries to adjust the configuration
to their desired requirements which should weigh up the balance between usability (allowing users to choose a password)
and security (preventing poor passwords).

- `compromised.password.thresholds.warning` Threshold by which a password is considered unsafe but within a known risk
value. Defaults to `1`
- `compromised.password.thresholds.severe` Threshold by which a password is considered completely unsafe. Defaults to `1`

#### Monitoring
- `compromised.password.monitoring.only` Enables/disables the recording of `PasswordCheckResults` via a User Defined
Metric recording mechanism. This allows implementers the monitor how poor input passwords are as a starting point or as
a business value propositionDefaults to `true`.

#### Example full YAML configuration with defaults

```yaml
compromised.password:
  service: pwnedpassword
  url: https://api.pwnedpasswords.com
  pwnedpasswords.prefix.length: 5
  thresholds:
    warning: 1
    severe: 1
  monitoring.only: true
```

#### Example full Properties configuration with defaults

```properties
compromised.password.service=pwnedpassword
compromised.password.url=https://api.pwnedpasswords.com
compromised.password.pwnedpasswords.prefix.length=5
compromised.password.thresholds.warning=1
compromised.password.thresholds.severe=1
compromised.password.monitoring.only=true
```

## Sample Module

The sample module acts as a very rudimentary test and sample bed for both libraries. It is designed to show a registration flow,
a login flow, password management flows and a change email flow and how the different libraries can be applied. It is
designed to **solely** show the annotations in action, the notification library in action, and the simple configuration.
It is in no way intended to implement a complete account or user security and management facility.

Since the sample is also tested against PwnedPasswords at a functional test level it also helps ensure that the
implementation of the libraries is correct according to the PwnedPasswords integration.

**DISCLAIMER** The sample is by no means complete, and by no means does it show a complete working model. Nor should it
be used in a live production environment.
