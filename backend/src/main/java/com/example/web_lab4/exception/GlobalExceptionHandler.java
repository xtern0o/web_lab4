package com.example.web_lab4.exception.handle;

import com.example.web_lab4.exception.exceptions.AlreadyAuthenticatedException;
import com.example.web_lab4.exception.exceptions.JwtExpiredException;
import com.example.web_lab4.exception.exceptions.UserAlreadyExistsException;
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

    private Map<String, Object> defaultErrorHandler(String message, HttpStatus status) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now());
        body.put("statusCode", status.value());
        body.put("status", status);
        body.put("error", message);

        return body;
    }

    private Map<String, Object> defaultInfoHandler(Exception e, HttpStatus status) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now());
        body.put("statusCode", status.value());
        body.put("status", status);
        body.put("info", e.getMessage());

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

    @ExceptionHandler(JwtExpiredException.class)
    public Map<String, Object> handleIllegalAccess(JwtExpiredException e) {
        return defaultErrorHandler("Срок действия JWT-токена истек. Подробнее: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AlreadyAuthenticatedException.class)
    public Map<String, Object> handleAlreadyAuthenticated(AlreadyAuthenticatedException e) {
        return defaultInfoHandler(e, HttpStatus.FOUND);
    }
}
