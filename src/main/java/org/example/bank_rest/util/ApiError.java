package org.example.bank_rest.util;

import jakarta.servlet.http.HttpServletRequest;
import org.example.bank_rest.dto.ApiErrorDto;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

public class ApiError {
    public static ApiErrorDto buildApiErrorDto(HttpStatus httpStatus, Exception ex, HttpServletRequest request) {
        return new ApiErrorDto(
            httpStatus.value(),
            httpStatus.getReasonPhrase(),
            ex.getMessage(),
            request.getRequestURI().replace("uri=", ""),
            OffsetDateTime.now()
        );
    }
}
