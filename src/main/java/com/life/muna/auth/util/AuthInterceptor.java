package com.life.muna.auth.util;

import com.life.muna.auth.dto.RefreshToken;
import com.life.muna.auth.repository.RefreshTokenRepository;
import com.life.muna.common.error.ErrorCode;
import com.life.muna.common.error.exception.BusinessException;
import com.life.muna.user.mapper.UserMapper;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    private AuthorizationExtractor extractor;
    private JwtTokenProvider tokenProvider;
    private RefreshTokenRepository refreshTokenRepository;

    public AuthInterceptor(AuthorizationExtractor extractor, JwtTokenProvider tokenProvider, RefreshTokenRepository refreshTokenRepository) {
        this.extractor = extractor;
        this.tokenProvider = tokenProvider;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = extractor.extract(request);
        Claims claims =  tokenProvider.validateToken(token);
        String emailFromToken = claims.getSubject();

        /**
        * 토큰에 저장된 이메일로 로그아웃된 회원인지 확인
        * */
        Optional<RefreshToken> rt = refreshTokenRepository.findByEmail(emailFromToken);
        if(rt.isEmpty()) throw new BusinessException(ErrorCode.INVALID_AUTH_TOKEN);
        request.setAttribute("email", emailFromToken);
        return true;
    }

}
