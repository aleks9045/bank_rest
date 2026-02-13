package org.example.bank_rest.exception.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * Enumeration for every error with "Bad request" status
 *

 */
@Getter
@RequiredArgsConstructor
public enum BadRequestError implements EnumError {
    BAD_REQUEST_ERROR("Плохой запрос.");

    private final String message;
    private final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

}
