package com.stuartbeard.iorek.function.breach;

import com.stuartbeard.iorek.external.hibp.exception.HIBPBadRequestException;
import com.stuartbeard.iorek.external.hibp.exception.HIBPServiceException;
import com.stuartbeard.iorek.external.hibp.exception.HIBPTooManyRequestsException;
import com.stuartbeard.iorek.service.model.IdentityInformation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.cloud.function.context.FunctionCatalog;
import org.springframework.cloud.function.context.test.FunctionalSpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static reactor.core.publisher.Flux.just;

@ExtendWith(SpringExtension.class)
@FunctionalSpringBootTest(classes = {CloudFunction.class, FunctionConfiguration.class})
@AutoConfigureWireMock(port = 8890)
@DirtiesContext
class BreachFunctionFunctionalTest {

    @Autowired
    private FunctionCatalog catalog;

    private Function<Flux<String>, Flux<IdentityInformation>> breachFunction;

    @BeforeEach
    void setupFunction() {
        breachFunction = catalog.lookup(Function.class, "breachFunction");
    }

    @Test
    void shouldShowBothBreachesAndPastesForGivenEmail() {
        IdentityInformation actualIdentityInformation = breachFunction.apply(just("breachandpaste@email.com")).blockFirst();

        assertThat(actualIdentityInformation).isNotNull();
        assertThat(actualIdentityInformation.getPastes()).hasSize(2);
        assertThat(actualIdentityInformation.getBreaches()).hasSize(2);
        assertThat(actualIdentityInformation.getMessage()).isEqualTo("Your email address appeared in 2 known data breaches, and 2 pastes.");
    }

    @Test
    void shouldShowOnlyBreachesForGivenEmail() {
        IdentityInformation actualIdentityInformation = breachFunction.apply(just("breachonly@email.com")).blockFirst();

        assertThat(actualIdentityInformation).isNotNull();
        assertThat(actualIdentityInformation.getPastes()).isEmpty();
        assertThat(actualIdentityInformation.getBreaches()).hasSize(2);
        assertThat(actualIdentityInformation.getMessage()).isEqualTo("Your email address appeared in 2 known data breaches, and 0 pastes.");
    }

    @Test
    void shouldShowOnlyPastesForGivenEmail() {
        IdentityInformation actualIdentityInformation = breachFunction.apply(just("pasteonly@email.com")).blockFirst();

        assertThat(actualIdentityInformation).isNotNull();
        assertThat(actualIdentityInformation.getPastes()).hasSize(2);
        assertThat(actualIdentityInformation.getBreaches()).isEmpty();
        assertThat(actualIdentityInformation.getMessage()).isEqualTo("Your email address appeared in 0 known data breaches, and 2 pastes.");
    }

    @Test
    void shouldShowOnlyNoBreachesOrPastesForGivenEmail() {
        Flux<String> notBreachedOrPasted = just("nothing@email.com");
        IdentityInformation actualIdentityInformation = breachFunction.apply(notBreachedOrPasted).blockFirst();

        assertThat(actualIdentityInformation).isNotNull();
        assertThat(actualIdentityInformation.getPastes()).isEmpty();
        assertThat(actualIdentityInformation.getBreaches()).isEmpty();
        assertThat(actualIdentityInformation.getMessage()).isEqualTo("Your email address appeared in 0 known data breaches, and 0 pastes.");
    }

    @Test
    void shouldThrowHIBPBadRequestExceptionWhenEmailBlank() {
        assertThrows(HIBPBadRequestException.class, () -> breachFunction.apply(just("")).blockFirst());
    }

    @Test
    void shouldThrowHIBPTooManyRequestsException() {
        assertThrows(HIBPTooManyRequestsException.class, () -> breachFunction.apply(just("toomanyrequests@email.com")).blockFirst());
    }

    @Test
    void shouldThrowHIBPServiceException() {
        assertThrows(HIBPServiceException.class, () -> breachFunction.apply(just("notauthorised@email.com")).blockFirst());
    }

}
