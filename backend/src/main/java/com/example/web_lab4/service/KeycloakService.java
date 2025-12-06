package com.example.web_lab4.service;

import com.example.web_lab4.config.KeycloakProperties;
import com.example.web_lab4.dto.request.AuthCodeRequestDto;
import com.example.web_lab4.dto.response.KeycloakTokenResponseDto;
import com.example.web_lab4.dto.response.TokenResponseDto;
import com.example.web_lab4.exception.exceptions.KeycloakTokenException;
import com.example.web_lab4.mapping.TokenResponseMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeycloakService {
    private final KeycloakProperties keycloakProperties;
    private final WebClient.Builder webClientBuilder;
    private final TokenResponseMapper tokenResponseMapper;

    /**
     * Обмен кода авторизации на токены доступа
     * @param authCodeRequestDto DTO кода от клиента
     * @return DTO для возврата токенов доступа клиенту
     */
    public TokenResponseDto exchangeCodeForTokens(AuthCodeRequestDto authCodeRequestDto) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "authorization_code");
        formData.add("code", authCodeRequestDto.getCode());
        formData.add("redirect_uri", authCodeRequestDto.getRedirectUri());
        formData.add("client_id", keycloakProperties.getClientId());
        formData.add("client_secret", keycloakProperties.getClientSecret());

        try {
            var keycloakTokenResponseDto = webClientBuilder.build()
                    .post()
                    .uri(keycloakProperties.getTokenUri())
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                    .body(BodyInserters.fromFormData(formData))
                    .retrieve()
                    .bodyToMono(KeycloakTokenResponseDto.class)
                    .block();

            if (keycloakTokenResponseDto == null) {
                throw new RuntimeException("Token Response from Keycloak is null");
            }

            for (String key : formData.keySet()) {
                log.info(key + ": " + formData.getFirst(key));
            }

            return tokenResponseMapper.fromKeycloakTokenResponseToTokenResponse(keycloakTokenResponseDto);

        } catch (WebClientResponseException e) {
            throw new KeycloakTokenException(e.getStatusCode(), e.getResponseBodyAsString());

        }

    }

    /**
     * Получить новый access token по refresh токену
     * @param refreshToken рефреш токен
     * @return TokenResponseDto, который нужно вернуть на клиент
     */
    public TokenResponseDto refreshAccessToken(String refreshToken) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "refresh_token");
        formData.add("client_id", keycloakProperties.getClientId());
        formData.add("client_secret", keycloakProperties.getClientSecret());
        formData.add("refresh_token", refreshToken);

        try {
            var keycloakTokenResponseDto = webClientBuilder.build()
                    .post()
                    .uri(keycloakProperties.getTokenUri())
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                    .body(BodyInserters.fromFormData(formData))
                    .retrieve()
                    .bodyToMono(KeycloakTokenResponseDto.class)
                    .block();
            if (keycloakTokenResponseDto == null) {
                throw new RuntimeException("token response is null");
            }

            if (keycloakTokenResponseDto.getAccessToken() == null) {
                throw new RuntimeException("access_token не получен от keycloak");
            }

            return tokenResponseMapper.fromKeycloakTokenResponseToTokenResponse(keycloakTokenResponseDto);
            
        } catch (WebClientResponseException e) {
            throw new KeycloakTokenException(e.getStatusCode(), e.getResponseBodyAsString());
        }
    }

    /**
     * Получить необходимую информацию для фронтенда
     * @return realm, auth_url, client_id
     */
    public Map<String, String> getConfig() {
        Map<String, String> config = new HashMap<>();
        config.put("auth_url", keycloakProperties.getAuthServerUrl());
        config.put("realm", keycloakProperties.getRealm());
        config.put("client_id", keycloakProperties.getClientId());
        return config;
    }

}
