package com.example.web_lab4.security.jwt;

import com.example.web_lab4.entity.UserEntity;
import com.example.web_lab4.service.TokenBlackListService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtUtils {
    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.token_expiration}")
    private Long expiration;

    /**
     * Генерация jwt токена по параметрам из конфигурации
     * @param userEntity entity пользователя
     * @return jwt токен
     */
    public String generateToken(UserEntity userEntity) {
        String jti = UUID.randomUUID().toString();

        Claims claims = Jwts.claims()
                .subject(userEntity.getUsername())
                .id(jti)
                .add("userId", userEntity.getId())
                .add("role", userEntity.getRole().name())
                .build();

        Date now = new Date();
        Date expiresAt = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(expiresAt)
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Для извлечения Jwt-токена из загаловка запроса
     * @param request Объект HttpServletRequest
     * @return null, если ключ по заголовку "Authorization" не пришел или не удовлетворяет стандартам JWT (не начинается с "Bearer ")
     */
    public Optional<String> extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) return Optional.empty();
        return bearerToken.substring(7).describeConstable();
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    private Claims getAllClaims(String token) {
        return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
    }

    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Date getExpirationDate(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    public boolean isExpired(String token) {
        return getExpirationDate(token).before(new Date());
    }

    public boolean isValidToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return !isExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Уникальный JTI для того чтобы не хранить целиком токены в блеклисте
     * @param token токен
     * @return JTI
     */
    public String getJti(String token) {
        return getClaim(token, Claims::getId);
    }

    public String getUsername(String token) {
        return getClaim(token, Claims::getSubject);
    }
}
