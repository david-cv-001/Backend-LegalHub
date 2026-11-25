package com.unsis.legalhub.common.exception;

import com.unsis.legalhub.common.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(
            ApiException exception, HttpServletRequest request) {
        ErrorResponse body = new ErrorResponse(
                Instant.now(),
                exception.getStatus().value(),
                exception.getError(),
                exception.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(exception.getStatus()).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException exception, HttpServletRequest request) {
        String message = exception.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .orElse("Validation failed");
        ErrorResponse body = new ErrorResponse(
                Instant.now(), 400, "Bad Request", message, request.getRequestURI());
        return ResponseEntity.badRequest().body(body);
    }
}
