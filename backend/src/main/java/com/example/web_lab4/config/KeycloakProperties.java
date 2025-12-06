package com.example.web_lab4.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "keycloak")
@Data
public class KeycloakProperties {
    /**
     * Имя реалма
     */
    private String realm;

    /**
     * Внешний адрес до сервера авторизации (кк)
     */
    private String authServerUrl;

    /**
     * Внутренний адрес до сервера авторизации в сети контейнера (в нашем случае keycloak:...)
     */
    private String internalAuthServerUrl;

    /**
     * id клиента в кк
     */
    private String clientId;

    /**
     * client secret нашего клиента
     */
    private String clientSecret;

    /**
     * URI для получения токенов
     */
    private String tokenUri;
}
