package com.life.muna.user.dto.profile;

import com.life.muna.user.domain.User;
import com.life.muna.user.domain.enums.UserLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserProfileResponse {
    private long userCode;
    private String email;
    private UserLevel userLevel;
    private String nickname;
    private byte[] profileImage;

    @Builder
    private UserProfileResponse(long userCode, String email, UserLevel userLevel, String nickname, byte[] profileImage) {
        this.userCode = userCode;
        this.email = email;
        this.userLevel = userLevel;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

    public static UserProfileResponse of(User user) {
        return UserProfileResponse.builder()
                .userCode(user.getUserCode())
                .email(user.getEmail())
                .userLevel(user.getUserLevel())
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .build();
    }
}
