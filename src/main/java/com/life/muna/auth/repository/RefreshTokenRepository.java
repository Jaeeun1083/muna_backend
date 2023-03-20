package com.life.muna.auth.repository;

import com.life.muna.auth.dto.RefreshToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
public class RefreshTokenRepository {
    private final RedisTemplate redisTemplate;
    private final long refreshTokenValidityTime;

    public RefreshTokenRepository(final RedisTemplate redisTemplate, @Value("${jwt.refresh-token-validity-time}") long refreshTokenValidityTime) {
        this.redisTemplate = redisTemplate;
        this.refreshTokenValidityTime = refreshTokenValidityTime * 1000;
    }

    public void save(final String email, final RefreshToken refreshToken) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(email, refreshToken.getRefreshToken());
        redisTemplate.expire(refreshToken.getRefreshToken(), refreshTokenValidityTime, TimeUnit.SECONDS);
    }

    public Optional<RefreshToken> findById(final String refreshToken) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String email = valueOperations.get(refreshToken);

        if (Objects.isNull(email)) {
            return Optional.empty();
        }

        return Optional.of(new RefreshToken(refreshToken));
    }

    public Optional<RefreshToken> findByEmail(final String email) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String refreshToken = valueOperations.get(email);

        if (Objects.isNull(refreshToken)) {
            return Optional.empty();
        }

        return Optional.of(new RefreshToken(refreshToken));
    }

}
