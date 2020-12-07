package com.stuartbeard.iorek.integration.test.context;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ScenarioContext {

    private final Map<String, Object> testContext = new HashMap<>();

    public <T> void setContext(ContextKey key, T value) {
        testContext.put(key.getKey(), value);
    }

    @SuppressWarnings("unchecked")
    public <T> T getContext(ContextKey key) {
        return (T) testContext.get(key.getKey());
    }

    public void clear() {
        testContext.clear();
    }
}
