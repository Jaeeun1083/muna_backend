package com.life.muna.auth.util;

import com.life.muna.auth.dto.AccessToken;
import com.life.muna.auth.dto.TokenResponse;
import com.life.muna.common.error.exception.BusinessException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

import static com.life.muna.common.error.ErrorCode.INVALID_AUTH_TOKEN;

@Component
public class JwtTokenProvider {
    private final Key secretKey;
    private final long accessTokenValidityTime;
    private final long refreshTokenValidityTime;

    public JwtTokenProvider(
            @Value("${jwt.token.secret-key}") String secretKey
            , @Value("${jwt.access-token-validity-time}") long accessTokenValidityTime
            , @Value("${jwt.refresh-token-validity-time}") long refreshTokenValidityTime
    ) {
        this.secretKey =  new SecretKeySpec(Base64.getDecoder().decode(secretKey), SignatureAlgorithm.HS256.getJcaName());
        this.accessTokenValidityTime = accessTokenValidityTime * 1000;
        this.refreshTokenValidityTime = refreshTokenValidityTime * 1000;
    }

    public TokenResponse createToken(String email) {
        String accessToken = createToken(email, accessTokenValidityTime);
        String refreshToken = createToken(email, refreshTokenValidityTime);


        System.out.println("accessToken : " + accessToken);
        System.out.println("refreshToken : " + refreshToken);
        return new TokenResponse(accessToken, refreshToken);
    }

    public AccessToken createAccessToken(String email) {
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

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch(ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | IllegalArgumentException e) {
            e.printStackTrace();
            throw new BusinessException(INVALID_AUTH_TOKEN);
        }
    }

}
