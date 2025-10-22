package com.example.web_lab4.utils.exception;

import org.springframework.security.core.AuthenticationException;

public class AuthException extends AuthenticationException {
  public AuthException(String message, Object... data) {
    super(String.format(message, data));
  }
}
