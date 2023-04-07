package com.life.muna.user.dto;

import com.life.muna.auth.dto.TokenResponse;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SignInResponse {
    private final Long userCode;
    private final TokenResponse token;

    @Builder
    public SignInResponse(Long userCode, TokenResponse tokenResponse) {
        this.userCode = userCode;
        this.token = tokenResponse;
    }

}
