package com.life.muna.user.dto;

import lombok.Getter;

@Getter
public class OtherUserProfile {
    private String userNickname;
    private byte[] userProfileImage;
    private String location;

    public OtherUserProfile(String userNickname, byte[] userProfileImage, String location) {
        this.userNickname = userNickname;
        this.userProfileImage = userProfileImage;
        this.location = location;
    }

}
