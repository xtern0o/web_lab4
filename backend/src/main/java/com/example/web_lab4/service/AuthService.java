package com.example.web_lab4.service;

import com.example.web_lab4.dto.request.UserRequestDto;
import com.example.web_lab4.dto.response.JwtResponseDto;
import com.example.web_lab4.entity.UserEntity;
import com.example.web_lab4.exception.exceptions.AlreadyAuthenticatedException;
import com.example.web_lab4.security.jwt.JwtUtils;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    /**
     * Регистрация пользователя и генерация jwt-токена для него
     * @param userRequestDto DTO пользователя
     * @return JWT-токен
     */
    public JwtResponseDto signUp(UserRequestDto userRequestDto) {
        UserEntity userEntity = userService.createUser(userRequestDto);

        String jwt = jwtUtils.generateToken(userEntity);
        return new JwtResponseDto(jwt, userEntity.getId(), userEntity.getUsername());
    }

    /**
     * Аутентификация пользователя
     * @param userRequestDto DTO пользователя
     * @return JWT-токен
     */
    public JwtResponseDto signIn(UserRequestDto userRequestDto) {
        if (isAuthenticated()) throw new AlreadyAuthenticatedException(userService.getByUsername(userRequestDto.getName()));

        UserEntity userEntity = userService.getByUsername(userRequestDto.getName());

        if (!passwordEncoder.matches(userRequestDto.getPassword(), userEntity.getPassword())) {
            throw new BadCredentialsException("Неверный пароль");
        }

        String jwt = jwtUtils.generateToken(userEntity);
        return new JwtResponseDto(jwt, userEntity.getId(), userEntity.getUsername());
    }

    public boolean isAuthenticated() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken);
    }

}
