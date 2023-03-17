package com.life.muna.auth.dto;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class AccessToken {
    private String accessToken;

    public AccessToken(@NonNull String accessToken) {
        this.accessToken = accessToken;
    }

}
