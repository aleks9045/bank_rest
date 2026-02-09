package org.example.bank_rest.exception.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * Enumeration for every error with "Conflict" status
 *
 * @author Aleksey
 */
@Getter
@RequiredArgsConstructor
public enum ConflictError implements EnumError {

    CONFLICT_ERROR("Конфликт сущности %s. %s.");

    private final String message;
    private final HttpStatus httpStatus = HttpStatus.CONFLICT;

}
