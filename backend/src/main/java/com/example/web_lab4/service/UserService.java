package com.example.web_lab4.service;

import com.example.web_lab4.dto.request.UserRequestDto;
import com.example.web_lab4.entity.UserEntity;
import com.example.web_lab4.mapping.UserMapper;
import com.example.web_lab4.repository.UserRepository;
import com.example.web_lab4.exception.exceptions.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AuthService authService;

    public UserEntity getByUsername(String username) {
        return userRepository.findByName(username).orElseThrow(
                () -> new UsernameNotFoundException(
                        String.format("Пользователь с именем '%s' не найден", username)
                )
        );
    }

    public UserEntity getCurrentUser() {
        Jwt jwt = authService.getCurrentJwt();
        UUID keycloakId = UUID.fromString(jwt.getSubject());
        // при первом обращении к серверу с таким jwt бизнес данные пользователя могут быть непроинициализированны
        return userRepository.findByKeycloakId(keycloakId).orElseGet(() -> createFromJwt(jwt));
    }

    public UserEntity getByKeycloakId(UUID keycloakId) {
        return userRepository
                .findByKeycloakId(keycloakId)
                .orElseThrow(
                        () -> new RuntimeException(
                                String.format("Пользователь с Keycloak Id = %s не найден", keycloakId.toString())
                        )
                );
    }

    @Transactional
    public UserEntity createFromJwt(Jwt jwt) {
        UUID keycloakId = UUID.fromString(jwt.getSubject());
        String username = jwt.getClaim("preferred_username");

        UserEntity userEntity = new UserEntity();
        userEntity.setName(username);
        userEntity.setKeycloakId(keycloakId);

        return userRepository.save(userEntity);
    }

}
