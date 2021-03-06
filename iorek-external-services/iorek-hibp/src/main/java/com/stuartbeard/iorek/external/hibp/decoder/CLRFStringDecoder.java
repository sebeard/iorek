package com.stuartbeard.iorek.external.hibp.decoder;

import feign.Response;
import feign.Util;
import feign.codec.DecodeException;
import feign.codec.Decoder;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;

@NoArgsConstructor
public class CLRFStringDecoder implements Decoder {

    public Object decode(Response response, Type type) throws IOException {
        Response.Body body = response.body();
        if (body == null) {
            return null;
        }

        if (String.class.equals(type)) {
            return Util.toString(body.asReader());
        }

        if (String[].class.equals(type)) {
            return Util.toString(body.asReader()).split("\\r\\n");
        }

        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            if (List.class.equals(parameterizedType.getRawType()) && Arrays.equals((new Type[]{String.class}), parameterizedType.getActualTypeArguments())) {
                return asList(Util.toString(body.asReader()).split("\\r\\n"));
            }
        }

        throw new DecodeException(response.status(), String.format("%s is not a type supported by this decoder.", type));

    }
}
