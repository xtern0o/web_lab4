package com.example.web_lab4.exception.exceptions;

import org.springframework.security.core.AuthenticationException;

public class UserAlreadyExistsException extends AuthenticationException {
  public UserAlreadyExistsException(String username) {
    super(String.format("Пользователь с именем '%s' уже существует", username));
  }
}
