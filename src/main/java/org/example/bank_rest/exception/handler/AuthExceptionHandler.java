package org.example.bank_rest.exception.handler;

import jakarta.servlet.http.HttpServletRequest;
import org.example.bank_rest.dto.ApiErrorDto;
import org.example.bank_rest.util.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;

/**
 * Authorization exception handler<br>
 * Handles AccessDeniedException and AuthenticationException that throws by spring security<br><br>
 *
 * Generates standardized responses using {@link ApiErrorDto}
 *
 * @author Aleksey
 */
@RestControllerAdvice
public class AuthExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorDto> handleAccessDeniedException(AccessDeniedException ex,
                                                                   HttpServletRequest request) {

        var httpStatus = HttpStatus.FORBIDDEN;

        var apiError = ApiError.buildApiErrorDto(httpStatus, ex, request);

        return ResponseEntity.status(httpStatus).body(apiError);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiErrorDto> handleSecurityException(AuthenticationException ex,
                                                               HttpServletRequest request) {

        var httpStatus = HttpStatus.UNAUTHORIZED;

        var apiError = ApiError.buildApiErrorDto(httpStatus, ex, request);

        return ResponseEntity.status(httpStatus).body(apiError);
    }
}

