package org.example.bank_rest.exception.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * Enumeration for every error with "Not found" status
 *

 */
@Getter
@RequiredArgsConstructor
public enum NotFoundError implements EnumError {

    NOT_FOUND_ERROR("Не найдено записи с id=%s.");

    private final String message;
    private final HttpStatus httpStatus = HttpStatus.NOT_FOUND;

}
