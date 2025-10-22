package com.example.web_lab4.service;

import com.example.web_lab4.dto.request.UserRequestDTO;
import com.example.web_lab4.dto.response.UserResponseDTO;
import com.example.web_lab4.entity.UserEntity;
import com.example.web_lab4.mapping.UserMapper;
import com.example.web_lab4.repository.UserRepository;
import com.example.web_lab4.utils.exception.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDTO registerUser(UserRequestDTO requestDTO) {
        if (userRepository.existsByName(requestDTO.getName())) {
            throw new AuthException("Пользователь с именем %s уже существует", requestDTO.getName());
        }

        UserEntity userEntity = userMapper.toEntity(requestDTO);
        userEntity.setPassword(passwordEncoder.encode(requestDTO.getPassword()));

        userRepository.save(userEntity);

        return userMapper.toResponseDTO(userEntity);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByName(username).orElseThrow(() -> new UsernameNotFoundException("Пользователь \"" + username + "\" не найден"));
    }
}
