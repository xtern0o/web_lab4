package com.example.web_lab4.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // TODO: разобраться с securityFilterChain да и в целом с SpringSecurity
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
//    }
}
