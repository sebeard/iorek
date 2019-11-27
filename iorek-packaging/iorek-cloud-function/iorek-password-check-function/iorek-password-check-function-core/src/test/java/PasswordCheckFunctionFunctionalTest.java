import com.stuartbeard.iorek.external.hibp.exception.HIBPBadRequestException;
import com.stuartbeard.iorek.external.hibp.exception.HIBPServiceException;
import com.stuartbeard.iorek.external.hibp.exception.HIBPTooManyRequestsException;
import com.stuartbeard.iorek.service.model.IdentityInformation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.cloud.function.context.FunctionCatalog;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;

import java.util.function.Function;

import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static reactor.core.publisher.Flux.just;

@ExtendWith(SpringExtension.class)
//@FunctionalSpringBootTest(classes = {CloudFunction.class, HIBPConfig.class})
@AutoConfigureWireMock
class PasswordCheckFunctionFunctionalTest {

    @Autowired
    private FunctionCatalog catalog;

    private Function<Flux<String>, Flux<IdentityInformation>> breachFunction;

    @BeforeEach
    void setupFunction() {
        breachFunction = catalog.lookup(Function.class, "passwordCheckFunction");
    }

    @Test
    @Disabled
    void shouldShowBothBreachesAndPastesForGivenEmail() {
        IdentityInformation actualIdentityInformation = breachFunction.apply(just("breachandpaste@email.com")).blockFirst();

        assertThat(actualIdentityInformation, is((notNullValue())));
        assertThat(actualIdentityInformation.getPastes(), hasSize(2));
        assertThat(actualIdentityInformation.getBreaches(), hasSize(2));
        assertThat(actualIdentityInformation.getMessage(), is("Your email address appeared in 2 known data breaches, and 2 pastes."));
    }

    @Test
    @Disabled
    void shouldShowOnlyBreachesForGivenEmail() {
        IdentityInformation actualIdentityInformation = breachFunction.apply(just("breachonly@email.com")).blockFirst();

        assertThat(actualIdentityInformation, is((notNullValue())));
        assertThat(actualIdentityInformation.getPastes(), is(emptyList()));
        assertThat(actualIdentityInformation.getBreaches(), hasSize(2));
        assertThat(actualIdentityInformation.getMessage(), is("Your email address appeared in 2 known data breaches, and 0 pastes."));
    }

    @Test
    @Disabled
    void shouldShowOnlyPastesForGivenEmail() {
        IdentityInformation actualIdentityInformation = breachFunction.apply(just("pasteonly@email.com")).blockFirst();

        assertThat(actualIdentityInformation, is((notNullValue())));
        assertThat(actualIdentityInformation.getPastes(), hasSize(2));
        assertThat(actualIdentityInformation.getBreaches(), is(emptyList()));
        assertThat(actualIdentityInformation.getMessage(), is("Your email address appeared in 0 known data breaches, and 2 pastes."));
    }

    @Test
    @Disabled
    void shouldShowOnlyNoBreachesOrPastesForGivenEmail() {
        IdentityInformation actualIdentityInformation = breachFunction.apply(just("nothing@email.com")).blockFirst();

        assertThat(actualIdentityInformation, is((notNullValue())));
        assertThat(actualIdentityInformation.getPastes(), is(emptyList()));
        assertThat(actualIdentityInformation.getBreaches(), is(emptyList()));
        assertThat(actualIdentityInformation.getMessage(), is("Your email address appeared in 0 known data breaches, and 0 pastes."));
    }

    @Test
    @Disabled
    void shouldThrowHIBPBadRequestExceptionWhenEmailBlank() {
        assertThrows(HIBPBadRequestException.class, () -> breachFunction.apply(just("")).blockFirst());
    }

    @Test
    @Disabled
    void shouldThrowHIBPTooManyRequestsException() {
        assertThrows(HIBPTooManyRequestsException.class, () -> breachFunction.apply(just("toomanyrequests@email.com")).blockFirst());
    }

    @Test
    @Disabled
    void shouldThrowHIBPServiceException() {
        assertThrows(HIBPServiceException.class, () -> breachFunction.apply(just("notauthorised@email.com")).blockFirst());
    }

}
