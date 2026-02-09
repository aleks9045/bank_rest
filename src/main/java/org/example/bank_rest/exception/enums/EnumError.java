package org.example.bank_rest.exception.enums;

import org.springframework.http.HttpStatus;

/**
 * Interface that unite all error enumerations
 *
 * @see BadRequestError Example of implementation
 * @author Aleksey
 */
public interface EnumError {

    String getMessage();
    HttpStatus getHttpStatus();

    default String format(Object... args) {
        return String.format(getMessage(), args);
    }
}
