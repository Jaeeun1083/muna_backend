package com.life.muna.user.dto;

import com.life.muna.auth.dto.TokenResponse;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SignInResponse {
    private final int userCode;
    private final TokenResponse token;

    @Builder
    public SignInResponse(int userCode, TokenResponse tokenResponse) {
        this.userCode = userCode;
        this.token = tokenResponse;
    }
}
