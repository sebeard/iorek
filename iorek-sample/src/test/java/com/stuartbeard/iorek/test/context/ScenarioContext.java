package com.stuartbeard.iorek.test.context;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
@RequiredArgsConstructor
public class ScenarioContext {

    private final Map<ContextKey, Object> context = new HashMap<>();

    public void put(ContextKey key, Object value) {
        context.put(key, value);
    }

    public <T> T get(ContextKey key) {
        return (T) context.get(key);
    }

    public boolean exists(ContextKey key) {
        return context.containsKey(key);
    }

    public <T> T getAs(ContextKey key, Class<T> clazz) {
        return clazz.cast(context.get(key));
    }

    public void clear() {
        context.clear();
    }

}
