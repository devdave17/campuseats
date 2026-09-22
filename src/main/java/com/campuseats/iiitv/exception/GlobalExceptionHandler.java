
package com.campuseats.iiitv.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Existing behavior: RuntimeException -> 404 Not Found
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

    // Existing behavior: validation/business input error -> 400 Bad Request
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

    // Part B: unsupported Accept header -> 406 Not Acceptable
    @ExceptionHandler(org.springframework.web.HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<Map<String, Object>> handleNotAcceptable(
            org.springframework.web.HttpMediaTypeNotAcceptableException ex) {

        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(
                Map.of(
                        "type", "https://campuseats.com/problems/not-acceptable",
                        "title", "Not Acceptable",
                        "status", 406,
                        "detail", "Only application/json responses are supported"
                )
        );
    }

    // Part B: unsupported Content-Type -> 415 Unsupported Media Type
    @ExceptionHandler(org.springframework.web.HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<Map<String, Object>> handleUnsupportedMediaType(
            org.springframework.web.HttpMediaTypeNotSupportedException ex) {

        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(
                Map.of(
                        "type", "https://campuseats.com/problems/unsupported-media-type",
                        "title", "Unsupported Media Type",
                        "status", 415,
                        "detail", "Only application/json request bodies are supported"
                )
        );
    }

    // Part B: missing/invalid Authorization header -> 401 Unauthorized
    @ExceptionHandler(org.springframework.web.bind.MissingRequestHeaderException.class)
    public ResponseEntity<Map<String, Object>> handleMissingHeader(
            org.springframework.web.bind.MissingRequestHeaderException ex) {

        if ("Authorization".equalsIgnoreCase(ex.getHeaderName())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .header("WWW-Authenticate", "Bearer")
                    .body(
                            Map.of(
                                    "type", "https://campuseats.com/problems/unauthorized",
                                    "title", "Unauthorized",
                                    "status", 401,
                                    "detail", "Authorization header is required"
                            )
                    );
        }

        return ResponseEntity.badRequest().body(
                Map.of(
                        "type", "https://campuseats.com/problems/bad-request",
                        "title", "Bad Request",
                        "status", 400,
                        "detail", "Required header is missing: " + ex.getHeaderName()
                )
        );
    }
}

