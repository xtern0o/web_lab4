package com.example.web_lab4.exception.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class KeycloakTokenException extends RuntimeException {
    private final HttpStatusCode status;
    private final String responseBody;

    public KeycloakTokenException(HttpStatusCode status, String responseBody) {
        super(String.format("Ошибка получения токена от Keycloak: %d. %s", status.value(), responseBody));
        this.status = status;
        this.responseBody = responseBody;
    }
}
