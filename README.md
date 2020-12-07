# Iorek

Iorek is a set of Spring boot and Spring Cloud function integrations against services providing breach data and related insights. 

It has the capability to check the perceived healthiness of a password; specifically has it been seen in a known leaked 
credential collection, and if so how many times. 

It also has the capability to monitor and detect if an email address being used by an individual has appeared in a data breach or a paste.

## Etymology

Iorek is inspired by the fantastic groundbreaking work produced by Troy Hunt that formed Have I Been Pwned (HIBP). Troy 
recently announced _Project Svalbard_ aimed at maintaining HIBP, and aiding the safe guarding of credentials on the web.

This project is named after Iorek Byrnison, the armoured bear from Philip Pullman's His Dark Materials Trilogy. Iorek 
Byrnison is an armoured bear and companion of Lyra's. Armoured bears, also known as panserbjørn in Norwegian, are a race 
of polar bear-like creatures with human level intelligence and opposable thumbs; they have no dæmons and consider their 
armour, which is made of meteoric iron, to be their soul.

Before the events of His Dark Materials, Iorek meets and befriends Lee Scoresby. Iorek was king of the armoured bears 
in Svalbard, but was exiled after killing another bear in a fight. Normally, when a bear realises that they are 
outmatched, they will surrender, but Iorek's opponent had been drugged by Iofur Raknison, second in line for the throne,
 to prevent this. Following his exile, the humans of an Arctic port town deceived him by giving him spirits and stealing
  his armour while he was intoxicated. Lyra finds him in Northern Lights working in a scrap yard for food and board and 
  proceeds to use her alethiometer to help him retrieve his armour. Following this, he helps Lyra, naming her 
  Silvertongue. When Lyra ends up imprisoned by Iofur Raknison, she arranges a fight between Iorek and Iofur for the 
  throne, which Iorek wins.
After the opening of a gate to Cittàgazze changes the climate of Svalbard, Iorek is forced to take his bears on a voyage
 down to the Himalayas, where he meets Will Parry. When Will breaks the Subtle Knife, Iorek is able to repair it without
  the aid of specialist equipment. He and a regiment of his subjects fight on Lord Asriel's side in the battle on the 
  plains; as part of this he takes Lyra and Will to find their lost dæmons. At the end of the Amber Spyglass, it is 
  revealed that he returns to Svalbard and reigns as king of the bears.

## References

 - Troy Hunt - Project Svalbard
 - Troy Hunt - 
 - HIBP
 - NCSC
 - Philip Pullman
 
## Running 
  
## Packaging and Deployment

### Docker Image

Available on Docker Hub under the image `iorek/iorek-api`. This is a spring boot application which operates a full 
REST API, and includes Swagger Documentation for testing and/or integration points.

### Password Check Cloud Function (AWS Lambda)

One of the artifacts produced from the packaging module is a fat jar file - `iorek-breach-function-aws-VERSION-aws.jar` 
which is deployable to AWS as a Lambda. This can be done manually via the AWS Console, via a CLI command;


```text
aws lambda create-function 
    --function-name Breach-Check 
    --role arn:aws:iam::[USERID]:role/service-role/[ROLE] 
    --zip-file fileb://path/to/iorek-passsword-check-function-aws-VERSION-aws.jar 
    --handler com.stuartbeard.iorek.function.breach.BreachFunctionHandler
    --description "Iorek Breach Check Function" 
    --runtime java8 
    --region [REGION]
    --timeout 60 
    --memory-size 192 
    --publish
```
 
or alongside a CloudFormation script. An example CloudFormation script is available in the `/samples` directory.

### Breach Check Cloud Function (AWS Lambda)

One of the artifacts produced from the packaging module is a fat jar file - `iorek-passsword-check-function-aws-VERSION-aws.jar` 
which is deployable to AWS as a Lambda. This can be done manually via the AWS Console, via a CLI command;


```text
aws lambda create-function 
    --function-name Password-Check 
    --role arn:aws:iam::[USERID]:role/service-role/[ROLE] 
    --zip-file fileb://path/to/iorek-passsword-check-function-aws-VERSION-aws.jar 
    --handler com.stuartbeard.iorek.function.password.check.PasswordCheckFunctionHandler
    --description "Iorek Password Check Function" 
    --runtime java8 
    --region [REGION]
    --timeout 60 
    --memory-size 192 
    --publish
```
 
or alongside a CloudFormation script. An example CloudFormation script is available in the `/samples` directory.

## Integrations and Configuration

Using this client integration should require minimum configuration. Most configuration values are defaulted, and the 
current (and only integration) is set to HIBP.

- `breach.service.name` determines which integration to use. At present that is defaulted to HIBP.

### HIBP (default)

 - `breach.service.name` Enables the HIBP integration (if there is a different default). To enable HIBP this must be set to `hibp` which is the current default.
 - `breach.service.url.pp` The API base URL to the Pwned Passwords API. At the time of writing this is `https://api.pwnedpasswords.com` which is the default.
 - `breach.service.url.hibp` The API base URL to the main HIBP API. At the time of writing this is `https://haveibeenpwned.com/api/v3` which is the default.
 - `breach.service.apikey` The Authentication Key required to use the main HIBP API. There is no default, and therefore you will need to purchase a key (for $3.50 a month, not bad at all). See here to purchase a key https://haveibeenpwned.com/API/Key and here to read up on the whys https://www.troyhunt.com/authentication-and-the-have-i-been-pwned-api
 - `breach.service.prefix.length` This the the required prefix length of the Hash provided to the Pwned Passwords REST service. Currently the requirement is for the first 5 characters, so this defaults to `5`.

## Other Configuration

### Environmental Overrides

All configuration can be overridden at a command line level using java properties overrides in the format 
`-Dproperty.name=value`, for example `-Dbreach.service.apikey=API-KEY-VALUE` sets the HIBP API Key which isn't 
defaulted, and `-Dcredential.safety.ok.threshold=5` overrides the default of 0 and sets it to 5.

Similarly environment variables can be set to override these values. Configuration properties change format to be come 
uppercase and underscore delimited, i.e. `credential.safety.warning.message` becomes `CREDENTIAL_SAFETY_WARNING_MESSAGE`.
 
### Credential Safety

This sets up the messaging and thresholds for how safe a password is considered. 

- Severe takes precedence over warning and ok thresholds
  - Setting the warning threshold higher than the severe threshold will have no affect. 
  - Setting the ok threshold higher than the severe threshold will mean that a severe message is returned but the credential marked as safe. 
- Setting the warning threshold equal to the ok or severe threshold will turn off warning messaging. 
- Setting the severe threshold equal to the ok threshold will mark credentials as allowed based solely off the `preventSevere` flag. 

**IMPORTANT NOTE** A password that does not appear in a data set is NOT necessarily safe, and does not mean it has NOT 
been compromised. It is merely a confidence indicator that it is not known whether or not a password has been compromised.

 - `credential.safety.ok.message` The message to return to the caller when an input password has been deemed to be safe, based on the associated threshold.
 - `credential.safety.ok.threshold` Threshold by which a password is considered safe. Defaults to `0`.`
 - `credential.safety.warning.message` The message to return to the caller when an input password has been deemed to be unsafe, but not so severely as to prevent the password being used, based on the associated threshold.
 - `credential.safety.warning.threshold` Threshold by which a password is considered unsafe but within a known risk value. Defaults to `0`
 - `credential.safety.severe.message` The message to return to the caller when an input password has been deemed to be too unsafe to be used, based on the associated threshold.
 - `credential.safety.severe.threshold` Threshold by which a password is considered completely unsafe. Defaults to `0`
 - `credential.safety.preventSevere` Flag that aides the setting the 'passwordAllowed' field in the return object if the severe threshold is exceeded; `false` allows all passwords to be used. Defaults to `true`




