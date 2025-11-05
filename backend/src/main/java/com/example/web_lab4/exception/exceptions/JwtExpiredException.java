package com.example.web_lab4.exception.exceptions;

public class JwtExpiredException extends RuntimeException {
    public JwtExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}