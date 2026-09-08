package com.campuseats.iiitv.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(
            RuntimeException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of(
                        "type", "https://campuseats.com/problems/not-found",
                        "title", "Resource Not Found",
                        "status", 404,
                        "detail", ex.getMessage()
                )
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest(
            IllegalArgumentException ex) {

        return ResponseEntity.badRequest().body(
                Map.of(
                        "type", "https://campuseats.com/problems/bad-request",
                        "title", "Bad Request",
                        "status", 400,
                        "detail", ex.getMessage()
                )
        );
    }
}