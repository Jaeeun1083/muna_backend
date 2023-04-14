package com.life.muna.user.dto;

import com.life.muna.user.domain.enums.UserLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserProfileResponse {
    private Long userCode;
    private String email;
    private UserLevel userLevel;
    private String nickname;
    private byte[] profileImage;

    @Builder
    public UserProfileResponse(Long userCode, String email, UserLevel userLevel, String nickname, byte[] profileImage) {
        this.userCode = userCode;
        this.email = email;
        this.userLevel = userLevel;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

}
