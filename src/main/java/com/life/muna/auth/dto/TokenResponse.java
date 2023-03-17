package com.life.muna.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class TokenResponse {
    private String accessToken;
    private String refreshToken;

    @Builder
    public TokenResponse(@NonNull String accessToken, @NonNull String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

}
