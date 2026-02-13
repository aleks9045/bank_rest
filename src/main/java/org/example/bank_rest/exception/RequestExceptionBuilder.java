package org.example.bank_rest.exception;



import org.example.bank_rest.exception.enums.EnumError;

import java.util.Arrays;
import java.util.stream.Stream;


public record RequestExceptionBuilder(EnumError enumError, Object... baseArgs) {

    public RequestException build() {
        return new RequestException(enumError, baseArgs);
    }

    public RequestException with(Object... additionalArgs) {
        final var finalArgs = Stream.concat(
            Arrays.stream(baseArgs),
            Arrays.stream(additionalArgs))
            .toArray();
        return new RequestException(enumError, finalArgs);
    }

}
