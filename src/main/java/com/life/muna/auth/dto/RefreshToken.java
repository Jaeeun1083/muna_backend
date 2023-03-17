package com.life.muna.auth.dto;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class RefreshToken {
    private String refreshToken;

    public RefreshToken(@NonNull String refreshToken) {
        this.refreshToken = refreshToken;
    }

}
