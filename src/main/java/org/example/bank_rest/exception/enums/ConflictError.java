package org.example.bank_rest.exception.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * Enumeration for every error with "Conflict" status
 *

 */
@Getter
@RequiredArgsConstructor
public enum ConflictError implements EnumError {

    NOT_ENOUGH_MEANS("Недостаточно средств для перевода."),
    INVALID_CARD_STATUS("Недопустимый статус (Заблокирована/Истек срок действия) карты с id=%s.");

    private final String message;
    private final HttpStatus httpStatus = HttpStatus.CONFLICT;

}
