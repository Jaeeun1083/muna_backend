package com.life.muna.user.service;

import com.life.muna.auth.dto.TokenResponse;
import com.life.muna.auth.util.JwtTokenProvider;
import com.life.muna.auth.util.PasswordEncoder;
import com.life.muna.common.error.ErrorCode;
import com.life.muna.common.error.exception.BusinessException;
import com.life.muna.common.error.exception.InputFieldException;
import com.life.muna.user.domain.User;
import com.life.muna.user.dto.SignInRequest;
import com.life.muna.user.dto.SignInResponse;
import com.life.muna.user.dto.SignUpRequest;
import com.life.muna.user.dto.UserProfileResponse;
import com.life.muna.user.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public UserService(UserMapper userMapper, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public Boolean signUp(SignUpRequest signUpRequest) {
        boolean isExistsUser = userMapper.existsByEmail(signUpRequest.getEmail());
        if (isExistsUser) throw new InputFieldException(ErrorCode.ALREADY_EXISTS_USER, signUpRequest.getEmail());
        User user = signUpRequest.toEntity(signUpRequest, passwordEncoder);
        int signUpResult = userMapper.signUp(user);
        return signUpResult == 1;
    }

    public SignInResponse signIn(SignInRequest signInRequest) {
        User user = userMapper.getUserByEmail(signInRequest.getEmail());
        if (user == null) throw new InputFieldException(ErrorCode.NOT_FOUND_MEMBER, signInRequest.getEmail());
        checkPassword(user, signInRequest.getPassword());
        return new SignInResponse(user.getUserCode(), jwtTokenProvider.createToken(user.getEmail()));
    }

    public UserProfileResponse getUserProfile(int userCode, String email) {
        User user = userMapper.getUserByUserCode(userCode);
        if (user == null) throw new InputFieldException(ErrorCode.NOT_FOUND_MEMBER, String.valueOf(userCode));
        if (!Objects.equals(user.getEmail(), email)) throw new InputFieldException(ErrorCode.INVALID_PARAMETER, String.valueOf(userCode));
        return UserProfileResponse.builder()
                .userCode(user.getUserCode())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .build();
    }

    private void checkPassword(User user, String password) {
        if (!user.getPassword().equals(passwordEncoder.encrypt(password))) {
            throw new BusinessException(ErrorCode.MISMATCH_MEMBER);
        }
    }

}
