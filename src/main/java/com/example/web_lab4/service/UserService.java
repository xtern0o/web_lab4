package com.example.web_lab4.service;

import com.example.web_lab4.dto.request.UserRequestDto;
import com.example.web_lab4.entity.UserEntity;
import com.example.web_lab4.mapping.UserMapper;
import com.example.web_lab4.repository.UserRepository;
import com.example.web_lab4.exception.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserEntity createUser(UserRequestDto requestDTO) {
        if (userRepository.existsByName(requestDTO.getName())) {
            throw new UserAlreadyExistsException(requestDTO.getName());
        }

        UserEntity userEntity = userMapper.toEntity(requestDTO);
        userEntity.setPassword(passwordEncoder.encode(requestDTO.getPassword()));

        return userRepository.save(userEntity);
    }

    public UserEntity getByUsername(String username) {
        return userRepository.findByName(username).orElseThrow(
                () -> new UsernameNotFoundException(
                        String.format("Пользователь с именем '%s' не найден", username)
                )
        );
    }
}
