package com.example.web_lab4.service;

import com.example.web_lab4.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenBlackListService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final JwtUtils jwtUtils;

    private final String BLACKLIST_PREFIX = "jwt:blacklist:";

    /**
     * Внесение jti токена в блеклист
     * @param token jwt
     */
    public void blacklistToken(String token) {
        String jti = jwtUtils.getJti(token);
        Date expiresAt = jwtUtils.getExpirationDate(token);

        long ttlMillis = expiresAt.getTime() - System.currentTimeMillis();

        if (ttlMillis > 0) {
            redisTemplate.opsForValue().set(
                    BLACKLIST_PREFIX + jti,
                    "blacklisted",
                    Duration.ofMillis(ttlMillis)
            );
        }
    }

    /**
     * Проверка: в блеклисте токен или нет
     * @param token jwt
     * @return в блеклисте
     */
    public boolean isBlacklisted(String token) {
        String jti = jwtUtils.getJti(token);
        return Boolean.TRUE.equals(redisTemplate.hasKey(BLACKLIST_PREFIX + jti));
    }
}
