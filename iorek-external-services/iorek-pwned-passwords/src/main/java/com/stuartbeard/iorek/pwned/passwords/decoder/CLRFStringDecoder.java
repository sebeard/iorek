/*
 * MIT License
 *
 * Copyright (c) 2019-2021 Stuart Beard
 */
package com.stuartbeard.iorek.pwned.passwords.decoder;

import feign.Response;
import feign.Util;
import feign.codec.DecodeException;
import feign.codec.Decoder;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * Custom decoder class that accepts line return feed String based responses and returns a single String an
 * array of Strings, or a List of String.
 * Specifically reads the response and then splits the response based on the regular expression <code>\\r\\n</code>
 * if needed and returns the result in the correctly required type.
 *
 * @author Stuart Beard
 * @version 1.0.0
 * @since 1.0.0
 */
@NoArgsConstructor
public class CLRFStringDecoder implements Decoder {

    @Override
    public Object decode(Response response, Type type) throws IOException {
        Response.Body body = response.body();
        if (body == null) {
            return null;
        }

        if (String.class.equals(type)) {
            return Util.toString(body.asReader(StandardCharsets.UTF_8));
        }

        if (String[].class.equals(type)) {
            return Util.toString(body.asReader(StandardCharsets.UTF_8)).split("\\r\\n");
        }

        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            if (List.class.equals(parameterizedType.getRawType()) && Arrays.equals((new Type[]{String.class}), parameterizedType.getActualTypeArguments())) {
                return Arrays.asList(Util.toString(body.asReader(StandardCharsets.UTF_8)).split("\\r\\n"));
            }
        }

        throw new DecodeException(response.status(), String.format("%s is not a type supported by this decoder.", type), response.request());

    }
}
