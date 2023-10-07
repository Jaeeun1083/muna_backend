package com.life.muna.user.dto.profile;

import com.life.muna.user.domain.enums.UserLevel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class OtherUserProfile {
    public String userNickname;
    public byte[] userProfileImage;
    public UserLevel userLevel;

    public OtherUserProfile(String userNickname, byte[] userProfileImage, UserLevel userLevel) {
        this.userNickname = userNickname;
        this.userProfileImage = userProfileImage;
        this.userLevel = userLevel;
    }

}
