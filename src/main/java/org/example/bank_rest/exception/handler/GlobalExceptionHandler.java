package org.example.bank_rest.exception.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import org.example.bank_rest.dto.ApiErrorDto;
import org.example.bank_rest.exception.RequestException;
import org.example.bank_rest.util.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.stream.Collectors;

/**
 * Global exception handler
 *
 * <p>Handles Request and other exceptions that throws from service package<br><br>
 *
 * Generates standardized responses using {@link ApiErrorDto}
 *
 * @author Aleksey
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RequestException.class)
    public ResponseEntity<ApiErrorDto> handleRequestException(RequestException ex,
                                                              HttpServletRequest request) {
        var httpStatus = ex.getHttpStatus();

        var apiError = new ApiErrorDto();

        return ResponseEntity.status(httpStatus).body(apiError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorDto> handleValidationException(MethodArgumentNotValidException ex,
                                                                 HttpServletRequest request) {
        var message = ex.getBindingResult().getFieldErrors()
            .stream()
            .map(err -> err.getField() + " " + err.getDefaultMessage())
            .collect(Collectors.joining("; "));

        var apiError = new ApiErrorDto(
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            message,
            request.getRequestURI(),
            OffsetDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiErrorDto> handleValidationException(ValidationException ex,
                                                                 HttpServletRequest request) {

        var httpStatus = HttpStatus.BAD_REQUEST;

        var apiError = ApiError.buildApiErrorDto(httpStatus, ex, request);

        return ResponseEntity.status(httpStatus).body(apiError);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorDto> handleRequestException(HttpServletRequest request) {
        var httpStatus = HttpStatus.BAD_REQUEST;

        var apiError = new ApiErrorDto(
            httpStatus.value(),
            httpStatus.getReasonPhrase(),
            "Invalid json",
            request.getRequestURI().replace("uri=", ""),
            OffsetDateTime.now()
        );

        return ResponseEntity.status(httpStatus).body(apiError);
    }
}
