package feign.codec;

import feign.Request;
import feign.RetryableException;
import feign.codec.ErrorDecoder.RetryAfterDecoder;

import java.util.Collection;
import java.util.Map;

import static feign.Util.RETRY_AFTER;

public abstract class RetryAfterException extends RetryableException {

    private static final long serialVersionUID = 1L;

    public RetryAfterException(int code, Map<String, Collection<String>> headers, String body) {
        super(code, body, Request.HttpMethod.GET, new RetryAfterDecoder().apply(retryAfterOrNull(headers)));
    }

    private static <T> T retryAfterOrNull(Map<String, Collection<T>> map) {
        if (map.containsKey(RETRY_AFTER) && !map.get(RETRY_AFTER).isEmpty()) {
            return map.get(RETRY_AFTER).iterator().next();
        }
        return null;
    }


}

