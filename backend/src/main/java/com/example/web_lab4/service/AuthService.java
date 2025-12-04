package com.example.web_lab4.service;

import com.example.web_lab4.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    /**
     * Получаем JWT текущего пользователя
     * @return jwt объект
     */
    public Jwt getCurrentJwt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Пользователь не авторизован");
        }

        if (!(authentication.getPrincipal() instanceof Jwt)) {
            throw new RuntimeException("Auth Principal не jwt");
        }

        return (Jwt) authentication.getPrincipal();
    }

    /**
     * Проверка, имеет ли текущий пользователь ту или иную роль
     * @param role роль БЕЗ префикса ROLE_
     * @return есть ли роль
     */
    public boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        return authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_" + role.toUpperCase()));
    }


}
