package com.life.muna.user.dto.signIn;

import com.life.muna.auth.dto.TokenResponse;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SignInResponse {
    private final long userCode;
    private final TokenResponse token;

    @Builder
    public SignInResponse(long userCode, TokenResponse tokenResponse) {
        this.userCode = userCode;
        this.token = tokenResponse;
    }

}
