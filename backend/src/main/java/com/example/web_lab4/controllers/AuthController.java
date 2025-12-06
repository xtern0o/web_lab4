package com.example.web_lab4.controllers;

import com.example.web_lab4.dto.request.AuthCodeRequestDto;
import com.example.web_lab4.dto.request.RefreshRequestDto;
import com.example.web_lab4.dto.response.TokenResponseDto;
import com.example.web_lab4.service.KeycloakService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final KeycloakService keycloakService;

    @GetMapping("/config")
    public Map<String, String> getAuthConfig() {
        return keycloakService.getConfig();
    }

    @PostMapping("/callback")
    public TokenResponseDto exchangeCodeForTokens(
            @Valid @RequestBody AuthCodeRequestDto authCodeRequestDto
    ) {
        return keycloakService.exchangeCodeForTokens(authCodeRequestDto);
    }

    @PostMapping("/refresh")
    public TokenResponseDto refreshToken(
            @Valid @RequestBody RefreshRequestDto refreshRequestDto
    ) {
        return keycloakService.refreshAccessToken(refreshRequestDto.getRefreshToken());
    }
}
