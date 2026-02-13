package org.example.bank_rest.exception.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * Enumeration for every error linked with auth
 *

 */
@Getter
@RequiredArgsConstructor
public enum AuthError implements EnumError {
    TOKENS_EXPIRED("Срок действия токена истек", HttpStatus.UNAUTHORIZED),
    INVALID_CREDENTIALS("Недостаточно полномочий", HttpStatus.FORBIDDEN),
    EMAIL_ALREADY_EXISTS("Почта уже существует", HttpStatus.FORBIDDEN),
    ACCESS_DENIED("Доступ запрещен", HttpStatus.FORBIDDEN);

    private final String message;
    private final HttpStatus httpStatus;
}
