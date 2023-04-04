package com.life.muna.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserProfileResponse {
    private int userCode;
    private String email;
    private String nickname;
    private byte[] profileImage;

    @Builder
    public UserProfileResponse(int userCode, String email, String nickname, byte[] profileImage) {
        this.userCode = userCode;
        this.email = email;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

}
