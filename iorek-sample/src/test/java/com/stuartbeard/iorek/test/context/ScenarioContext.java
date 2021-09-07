/*
 * MIT License
 *
 * Copyright (c) 2019-2021 Stuart Beard
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
