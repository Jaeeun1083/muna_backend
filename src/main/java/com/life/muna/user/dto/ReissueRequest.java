package com.life.muna.user.dto;

import com.life.muna.auth.dto.RefreshToken;
import lombok.Getter;

@Getter
public class ReissueRequest {
    private String email;
    private RefreshToken refreshToken;
}
