package com.stuartbeard.iorek.integration;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ScenarioContext {

    private Map<String, Object> testContext = new HashMap<>();

    public <T> void setContext(String key, T value) {
        testContext.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T getContext(String key) {
        return (T) testContext.get(key);
    }

    public void clear() {
        testContext.clear();
    }
}
