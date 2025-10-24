package com.example.web_lab4.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private Map<String, Object> defaultErrorHandler(Exception e, HttpStatus status) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now());
        body.put("statusCode", status.value());
        body.put("status", status);
        body.put("error", e.getMessage());

        return body;
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public Map<String, Object> handleUserAlreadyExists(UserAlreadyExistsException e) {
        return defaultErrorHandler(e, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public Map<String, Object> handleBadCredentials(BadCredentialsException e) {
        return defaultErrorHandler(e, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public Map<String, Object> handleUserNotFound(UsernameNotFoundException e) {
        return defaultErrorHandler(e, HttpStatus.NOT_FOUND);
    }
}
