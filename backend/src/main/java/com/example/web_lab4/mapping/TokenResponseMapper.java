package com.example.web_lab4.mapping;

import com.example.web_lab4.dto.response.KeycloakTokenResponseDto;
import com.example.web_lab4.dto.response.TokenResponseDto;
import org.springframework.stereotype.Component;

@Component
public class TokenResponseMapper {
    public TokenResponseDto fromKeycloakTokenResponseToTokenResponse(KeycloakTokenResponseDto keycloakTokenResponseDto) {
        return TokenResponseDto.builder()
                .accessToken(keycloakTokenResponseDto.getAccessToken())
                .refreshToken(keycloakTokenResponseDto.getRefreshToken())
                .idToken(keycloakTokenResponseDto.getIdToken())
                .tokenType(keycloakTokenResponseDto.getTokenType())
                .expiresIn(keycloakTokenResponseDto.getExpiresIn())
                .refreshExpiresIn(keycloakTokenResponseDto.getRefreshExpiresIn())
                .build();
    }
}
