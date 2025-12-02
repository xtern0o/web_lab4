package com.example.web_lab4.security.filter;

import com.example.web_lab4.entity.UserEntity;
import com.example.web_lab4.exception.exceptions.JwtExpiredException;
import com.example.web_lab4.security.jwt.JwtUtils;
import com.example.web_lab4.service.UserDetailsServiceImpl;
import com.example.web_lab4.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String token = jwtUtils.extractJwtFromRequest(request).orElse(null);
            if (token == null) {
                filterChain.doFilter(request, response);
                return;
            }
            if (!jwtUtils.isValidToken(token)) {
                filterChain.doFilter(request, response);
                return;
            }
            String username = jwtUtils.getUsername(token);
            UserDetails user = userDetailsService.loadUserByUsername(username);

            var authToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);

        } catch (ExpiredJwtException expiredJwtException) {
            throw new JwtExpiredException(expiredJwtException.getMessage(), expiredJwtException);
        }

        filterChain.doFilter(request, response);
    }
}
