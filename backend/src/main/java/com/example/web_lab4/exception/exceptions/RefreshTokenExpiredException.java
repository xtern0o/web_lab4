package com.example.web_lab4.exception.exceptions;

public class RefreshTokenExpiredException extends RuntimeException {
    public RefreshTokenExpiredException(String token) {
        super(String.format("refresh token is out of date: %s", token));
    }
}
