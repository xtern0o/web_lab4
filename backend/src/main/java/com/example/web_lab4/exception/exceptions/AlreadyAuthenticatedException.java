package com.example.web_lab4.exception.exceptions;

import com.example.web_lab4.entity.UserEntity;

public class AlreadyAuthenticatedException extends RuntimeException {
    public AlreadyAuthenticatedException(UserEntity user) {
        super(String.format("Пользователь \"%s\" уже авторизован", user.getName()));
    }
}
