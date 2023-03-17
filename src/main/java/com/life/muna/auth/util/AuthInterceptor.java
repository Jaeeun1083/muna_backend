package com.life.muna.auth.util;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    private AuthorizationExtractor extractor;
    private JwtTokenProvider tokenProvider;

    public AuthInterceptor(AuthorizationExtractor extractor, JwtTokenProvider tokenProvider) {
        this.extractor = extractor;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = extractor.extract(request);
        return tokenProvider.validateToken(token);
    }

}
