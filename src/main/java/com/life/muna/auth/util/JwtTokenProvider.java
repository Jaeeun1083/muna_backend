package com.life.muna.auth.util;

import com.life.muna.auth.dto.AccessToken;
import com.life.muna.auth.dto.RefreshToken;
import com.life.muna.auth.dto.TokenResponse;
import com.life.muna.auth.repository.RefreshTokenRepository;
import com.life.muna.common.error.exception.BusinessException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

import static com.life.muna.common.error.ErrorCode.*;

@Component
public class JwtTokenProvider {
    private final Key secretKey;
    private final long accessTokenValidityTime;
    private final long refreshTokenValidityTime;
    private final RefreshTokenRepository refreshTokenRepository;

    public JwtTokenProvider(
            @Value("${jwt.token.secret-key}") String secretKey
            , @Value("${jwt.access-token-validity-time}") long accessTokenValidityTime
            , @Value("${jwt.refresh-token-validity-time}") long refreshTokenValidityTime
            , final RefreshTokenRepository refreshTokenRepository
    ) {
        this.secretKey =  new SecretKeySpec(Base64.getDecoder().decode(secretKey), SignatureAlgorithm.HS256.getJcaName());
        this.accessTokenValidityTime = accessTokenValidityTime * 1000; // 3600초
        this.refreshTokenValidityTime = refreshTokenValidityTime * 1000;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public TokenResponse createToken(String email) {
        Optional<RefreshToken> refreshTokenFromRedis = refreshTokenRepository.findByEmail(email);
        if(refreshTokenFromRedis.isPresent()) throw new BusinessException(ALREADY_LOGIN_USER);
        String accessToken = createToken(email, accessTokenValidityTime);
        String refreshToken = createToken(email, refreshTokenValidityTime);
        refreshTokenRepository.save(email, new RefreshToken(refreshToken));
        return new TokenResponse(accessToken, refreshToken);
    }

    public AccessToken createAccessToken(String email, RefreshToken refreshToken) {
        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByEmail(email);
        if(optionalRefreshToken.isEmpty()) throw new BusinessException(INVALID_PROVIDER);
        if(!optionalRefreshToken.get().getRefreshToken().equals(refreshToken.getRefreshToken())) throw new BusinessException(INVALID_AUTH_TOKEN);
//                .orElseThrow(new BusinessException(INVALID_PROVIDER));
        return new AccessToken(createToken(email, accessTokenValidityTime));
    }

    private String createToken(String email, long validityTime) {
        Claims claims = Jwts.claims().setSubject(email);
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityTime);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(secretKey)
                .compact();
    }

    public Claims validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            String email = claims.getSubject();

            if(refreshTokenRepository.findByEmail(email).isEmpty()) throw new BusinessException(INVALID_AUTH_TOKEN);
            return claims;
        } catch(ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | IllegalArgumentException e) {
            e.printStackTrace();
            throw new BusinessException(INVALID_AUTH_TOKEN);
        }
    }

}
