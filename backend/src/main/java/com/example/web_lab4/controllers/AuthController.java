package com.example.web_lab4.controllers;

import com.example.web_lab4.dto.request.UserRequestDto;
import com.example.web_lab4.dto.response.JwtResponseDto;
import com.example.web_lab4.security.jwt.JwtUtils;
import com.example.web_lab4.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public JwtResponseDto signUp(@RequestBody UserRequestDto userRequestDto) {
        return authService.signUp(userRequestDto);
    }

    @PostMapping("/signin")
    public JwtResponseDto signIn(@RequestBody UserRequestDto userRequestDto) {
        return authService.signIn(userRequestDto);
    }
}
