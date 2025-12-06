package com.example.web_lab4.exception;

import com.example.web_lab4.exception.exceptions.AlreadyAuthenticatedException;
import com.example.web_lab4.exception.exceptions.JwtExpiredException;
import com.example.web_lab4.exception.exceptions.KeycloakTokenException;
import com.example.web_lab4.exception.exceptions.UserAlreadyExistsException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final ObjectMapper objectMapper;

    private Map<String, Object> defaultErrorHandler(Exception e) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now());
        body.put("error", e.getMessage());

        return body;
    }

    private Map<String, Object> defaultErrorHandler(String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now());
        body.put("error", message);

        return body;
    }

    private Map<String, Object> defaultInfoHandler(Exception e) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now());
        body.put("info", e.getMessage());

        return body;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleValidationExceptions(MethodArgumentNotValidException e) {
        return defaultErrorHandler(
                e.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .findFirst()
                        .map(FieldError::getDefaultMessage)
                        .orElse("Ошибка валидации")
        );
    }

    @ExceptionHandler(KeycloakTokenException.class)
    public ResponseEntity<Map<String, Object>> handleKeycloakToken(KeycloakTokenException e) {

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", Instant.now());
        errorResponse.put("status", e.getStatus().value());
        try {
            JsonNode keycloakError = objectMapper.readTree(e.getResponseBody());

            String error = keycloakError.path("error").asText("unknown_error");
            String errorDescription = keycloakError.path("error_description").asText(e.getMessage());

            errorResponse.put("keycloak_details", objectMapper.convertValue(keycloakError, Map.class));

        } catch (Exception parseException) {
            errorResponse.put("error", "keycloak_error");
            errorResponse.put("error_description", e.getMessage());
            errorResponse.put("raw_response", e.getResponseBody());
        }

        return ResponseEntity.status(e.getStatus()).body(errorResponse);

    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, Object> handleUserAlreadyExists(UserAlreadyExistsException e) {
        return defaultErrorHandler(e);
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Map<String, Object> handleBadCredentials(BadCredentialsException e) {
        return defaultErrorHandler(e);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handleUserNotFound(UsernameNotFoundException e) {
        return defaultErrorHandler(e);
    }

    @ExceptionHandler(JwtExpiredException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Map<String, Object> handleIllegalAccess(JwtExpiredException e) {
        return defaultErrorHandler("Срок действия JWT-токена истек. Перезайдите в аккаунт. Подробнее: " + e.getMessage());
    }

    @ExceptionHandler(AlreadyAuthenticatedException.class)
    @ResponseStatus(HttpStatus.FOUND)
    public Map<String, Object> handleAlreadyAuthenticated(AlreadyAuthenticatedException e) {
        return defaultInfoHandler(e);
    }

}
