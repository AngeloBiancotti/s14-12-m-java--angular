package com.example.app.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    // Validations errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApplicationExceptionResponse> methodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest req) {
        Map<String, String> errors = ex.getFieldErrors().stream().collect(
          Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)
        );

        ApplicationExceptionResponse errorResponse = ExceptionUtils.createResponse(HttpStatus.BAD_REQUEST, req, errors);
        return ResponseEntity.status(400).body(errorResponse);

    }

    // DB Integrity Violation
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApplicationExceptionResponse> dataIntegrityViolationException(DataIntegrityViolationException ex, HttpServletRequest req) {

        Map<String, String> errors = new HashMap<>(Map.of(ex.getClass().getSimpleName(), ex.getMessage()));
        ApplicationExceptionResponse errorResponse = ExceptionUtils.createResponse(HttpStatus.BAD_REQUEST, req, errors);
        return ResponseEntity.status(400).body(errorResponse);

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
    }

}

