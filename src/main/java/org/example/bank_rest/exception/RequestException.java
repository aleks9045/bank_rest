package org.example.bank_rest.exception;

import lombok.Getter;
import org.example.bank_rest.exception.enums.EnumError;
import org.springframework.http.HttpStatus;

/**
 * Exception that throws when business logic violated
 *
 * <p> Stores error message and http status code of error<br>
 * In constructor accepts {@link EnumError}
 *
 * @author Aleksey
 */
@Getter
public class RequestException extends RuntimeException {
    private final HttpStatus httpStatus;

    public RequestException(EnumError enumError, Object... args) {
        super(enumError.format(args));
        this.httpStatus = enumError.getHttpStatus();
    }
}
