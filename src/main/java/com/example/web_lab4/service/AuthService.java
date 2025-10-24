package com.example.web_lab4.service;

import com.example.web_lab4.dto.request.UserRequestDto;
import com.example.web_lab4.dto.response.JwtResponseDto;
import com.example.web_lab4.entity.UserEntity;
import com.example.web_lab4.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
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
        return new JwtResponseDto(jwt);
    }

    /**
     * Аутентификация пользователя
     * @param userRequestDto DTO пользователя
     * @return JWT-токен
     */
    public JwtResponseDto signIn(UserRequestDto userRequestDto) {
        UserEntity userEntity = userService.getByUsername(userRequestDto.getName());

        if (!passwordEncoder.matches(userRequestDto.getPassword(), userEntity.getPassword())) {
            throw new BadCredentialsException("Неверный пароль");
        }

        String jwt = jwtUtils.generateToken(userEntity);
        return new JwtResponseDto(jwt);
    }

}
