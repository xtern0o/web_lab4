package com.example.web_lab4.exception.exceptions;

public class AuthorizationCodeExpiredException extends RuntimeException {
    public AuthorizationCodeExpiredException(String token) {
        super("Authorization Code is expired: " + token);
    }
}
