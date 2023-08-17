package com.life.muna.user.dto.profile;

import com.life.muna.mcoin.domain.Mcoin;
import com.life.muna.user.domain.User;
import com.life.muna.user.domain.enums.UserLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserProfileResponse {
    private long userCode;
    private String email;
    private String nickname;
    private UserLevel userLevel;
    private byte[] profileImage;
    private int reqCnt;
    private int mcoin;

    @Builder
    public UserProfileResponse(long userCode, String email, String nickname, UserLevel userLevel, byte[] profileImage, int reqCnt, int mcoin) {
        this.userCode = userCode;
        this.email = email;
        this.nickname = nickname;
        this.userLevel = userLevel;
        this.profileImage = profileImage;
        this.reqCnt = reqCnt;
        this.mcoin = mcoin;
    }

    public static UserProfileResponse of(User user, Mcoin mcoin) {
        return UserProfileResponse.builder()
                .userCode(user.getUserCode())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .userLevel(user.getUserLevel())
                .profileImage(user.getProfileImage())
                .reqCnt(user.getReqCnt() + user.getCashedReqCnt())
                .mcoin(mcoin.getTotalAmount())
                .build();
    }

}
