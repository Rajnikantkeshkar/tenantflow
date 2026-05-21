package com.tenantflow.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /*
     * Handle Resource Not Found
     */
    @ExceptionHandler(ResourceNotFoundException.class)

    public ResponseEntity<Map<String, Object>>
    handleResourceNotFound(
            ResourceNotFoundException ex) {
        log.warn("Resource Not Found: {}", ex.getMessage());

        Map<String, Object> error = new HashMap<>();

        error.put("timestamp", LocalDateTime.now());

        error.put("status", 404);

        error.put("error", "Not Found");

        error.put("message", ex.getMessage());

        return new ResponseEntity<>(
                error,
                HttpStatus.NOT_FOUND
        );
    }

    /*
     * Handle Resource Already Exists
     */
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleResourceAlreadyExists(
            ResourceAlreadyExistsException ex) {
        log.warn("Resource Already Exists: {}", ex.getMessage());

        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", 409);
        error.put("error", "Conflict");
        error.put("message", ex.getMessage());

        return new ResponseEntity<>(
                error,
                HttpStatus.CONFLICT
        );
    }

    /*
     * Handle Validation Errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        log.warn("Validation failed for request");

        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", 400);
        error.put("error", "Bad Request");

        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(e -> 
            fieldErrors.put(e.getField(), e.getDefaultMessage())
        );

        error.put("message", "Validation failed");
        error.put("details", fieldErrors);

        return new ResponseEntity<>(
                error,
                HttpStatus.BAD_REQUEST
        );
    }

    /*
     * Handle JSON Parse Errors (e.g. invalid UUID format)
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleMessageNotReadable(
            HttpMessageNotReadableException ex) {
        log.warn("Malformed JSON request: {}", ex.getMessage());

        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", 400);
        error.put("error", "Bad Request");
        error.put("message", "Malformed JSON request. Please check your data types (e.g. valid UUID formats).");

        return new ResponseEntity<>(
                error,
                HttpStatus.BAD_REQUEST
        );
    }

    /*
     * Handle Database Integrity Errors (e.g., Duplicate Email)
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolation(
            DataIntegrityViolationException ex) {
        log.warn("Database error: {}", ex.getMessage());

        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", 409);
        error.put("error", "Conflict");
        error.put("message", "Data integrity violation. This could be caused by a duplicate value (like email) that must be unique.");

        return new ResponseEntity<>(
                error,
                HttpStatus.CONFLICT
        );
    }

    /*
     * Handle UpgradeRequiredException (402 Payment Required)
     */
    @ExceptionHandler(UpgradeRequiredException.class)
    public ResponseEntity<Map<String, Object>> handleUpgradeRequiredException(
            UpgradeRequiredException ex) {
        log.warn("Upgrade Required: {}", ex.getMessage());

        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("status", HttpStatus.PAYMENT_REQUIRED.value());
        errorDetails.put("error", "Payment Required");
        errorDetails.put("message", ex.getMessage());

        return new ResponseEntity<>(errorDetails, HttpStatus.PAYMENT_REQUIRED);
    }

    /*
     * Rate Limit Exception
     */
    @ExceptionHandler(RateLimitException.class)
    public ResponseEntity<Map<String, Object>> handleRateLimit(RateLimitException ex) {

        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", HttpStatus.TOO_MANY_REQUESTS.value());
        error.put("error", "Too Many Requests");
        error.put("message", ex.getMessage());

        return new ResponseEntity<>(error, HttpStatus.TOO_MANY_REQUESTS);
    }

}