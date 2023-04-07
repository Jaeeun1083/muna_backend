package com.life.muna.auth.util;

import com.life.muna.common.error.ErrorCode;
import com.life.muna.common.error.exception.BusinessException;
import com.life.muna.user.mapper.UserMapper;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    private AuthorizationExtractor extractor;
    private JwtTokenProvider tokenProvider;
    private UserMapper userMapper;

    public AuthInterceptor(AuthorizationExtractor extractor, JwtTokenProvider tokenProvider, UserMapper userMapper) {
        this.extractor = extractor;
        this.tokenProvider = tokenProvider;
        this.userMapper = userMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = extractor.extract(request);
        Claims claims =  tokenProvider.validateToken(token);
        String emailFromToken = claims.getSubject();
        Long userCode = Long.parseLong(request.getParameter("userCode"));
        String findEmail = userMapper.findEmailByUserCode(userCode);
        if (findEmail == null || findEmail.isEmpty()) throw new BusinessException(ErrorCode.NOT_FOUND_BY_USER_CODE);
        if (!findEmail.equals(emailFromToken)) throw new BusinessException(ErrorCode.MISMATCH_TOKEN_USER_CODE);
        return true;
    }

}
