import com.stuartbeard.iorek.passwordcheck.CloudFunction;
import com.stuartbeard.iorek.passwordcheck.FunctionConfiguration;
import com.stuartbeard.iorek.passwordcheck.model.PasswordCheckRequest;
import com.stuartbeard.iorek.service.model.CredentialSafety;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.cloud.function.context.FunctionCatalog;
import org.springframework.cloud.function.context.test.FunctionalSpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;

import java.util.function.Function;

import static org.apache.commons.codec.digest.DigestUtils.sha1Hex;
import static org.assertj.core.api.Assertions.assertThat;
import static reactor.core.publisher.Flux.just;

@ExtendWith(SpringExtension.class)
@FunctionalSpringBootTest(classes = {CloudFunction.class, FunctionConfiguration.class})
@AutoConfigureWireMock(port = 8889)
@DirtiesContext
class PasswordCheckingFunctionFunctionalTest {

    @Autowired
    private FunctionCatalog catalog;

    private Function<Flux<PasswordCheckRequest>, Flux<CredentialSafety>> passwordCheckFunction;

    @BeforeEach
    void setupFunction() {
        passwordCheckFunction = catalog.lookup(Function.class, "passwordCheckingFunction");
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void shouldReturnUnsafeAndDisallowedForOverlyCompromisedPassword(boolean sha1Hash) {
        PasswordCheckRequest passwordCheckRequest = new PasswordCheckRequest();
        passwordCheckRequest.setPassword(sha1Hash ? sha1Hex("Password1") : "Password1");
        passwordCheckRequest.setSha1Hash(sha1Hash);

        CredentialSafety credentialSafety = passwordCheckFunction.apply(just(passwordCheckRequest)).blockFirst();

        assertThat(credentialSafety).isNotNull();
        assertThat(credentialSafety.getAppearancesInDataSet()).isEqualTo(139705);
        assertThat(credentialSafety.isPasswordAllowed()).isFalse();
        assertThat(credentialSafety.isSafe()).isFalse();
        assertThat(credentialSafety.getMessage()).isEqualTo("credential.safety.severe");
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void shouldReturnUnsafeButAllowedForCompromisedPassword(boolean sha1Hash) {
        PasswordCheckRequest passwordCheckRequest = new PasswordCheckRequest();
        passwordCheckRequest.setPassword(sha1Hash ? sha1Hex("bobthebuilder1") : "bobthebuilder1");
        passwordCheckRequest.setSha1Hash(sha1Hash);

        CredentialSafety credentialSafety = passwordCheckFunction.apply(just(passwordCheckRequest)).blockFirst();

        assertThat(credentialSafety).isNotNull();
        assertThat(credentialSafety.getAppearancesInDataSet()).isEqualTo(339);
        assertThat(credentialSafety.isPasswordAllowed()).isTrue();
        assertThat(credentialSafety.isSafe()).isFalse();
        assertThat(credentialSafety.getMessage()).isEqualTo("credential.safety.warning");
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void shouldReturnSafeAndAllowedForUncompromisedPassword(boolean sha1Hash) {
        PasswordCheckRequest passwordCheckRequest = new PasswordCheckRequest();
        passwordCheckRequest.setPassword(sha1Hash ? sha1Hex("this-password-would-never-appear-in-a-breach") : "this-password-would-never-appear-in-a-breach");
        passwordCheckRequest.setSha1Hash(sha1Hash);

        CredentialSafety credentialSafety = passwordCheckFunction.apply(just(passwordCheckRequest)).blockFirst();

        assertThat(credentialSafety).isNotNull();
        assertThat(credentialSafety.getAppearancesInDataSet()).isZero();
        assertThat(credentialSafety.isPasswordAllowed()).isTrue();
        assertThat(credentialSafety.isSafe()).isTrue();
        assertThat(credentialSafety.getMessage()).isEqualTo("credential.safety.ok");
    }

}
