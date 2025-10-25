package com.example.web_lab4.security.handler;

import com.example.web_lab4.security.jwt.JwtUtils;
import com.example.web_lab4.service.TokenBlackListService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LogoutHandlerImpl implements LogoutHandler {
    private final JwtUtils jwtUtils;
    private final TokenBlackListService tokenBlackListService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String token = jwtUtils.extractJwtFromRequest(request).orElse(null);
        if (token == null) return;

        if (!tokenBlackListService.isBlacklisted(token)) {
            tokenBlackListService.blacklistToken(token);
        }

    }
}
