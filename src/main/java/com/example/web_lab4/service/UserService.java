package com.example.web_lab4.service;

import com.example.web_lab4.mapping.UserMapper;
import com.example.web_lab4.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;


}
