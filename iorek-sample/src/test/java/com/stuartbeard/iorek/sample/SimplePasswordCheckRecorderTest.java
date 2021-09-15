package com.stuartbeard.iorek.sample;

import com.stuartbeard.iorek.service.model.PasswordCheckResult;
import com.stuartbeard.iorek.service.model.PasswordRiskLevel;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SimplePasswordCheckRecorderTest {

    private final SimplePasswordCheckRecorder recorder = new SimplePasswordCheckRecorder();

    @Test
    void shouldRecordPasswordCheckResultIntoMap() {
        PasswordCheckResult passwordCheckResult = new PasswordCheckResult();
        passwordCheckResult.setCompromisedCount(12345);
        passwordCheckResult.setRiskLevel(PasswordRiskLevel.COMPROMISED);

        recorder.recordPasswordCheck("requestFlow", passwordCheckResult);

        assertThat(recorder.getLatestPasswordCheckResult("requestFlow")).isEqualTo(passwordCheckResult);
    }

}
