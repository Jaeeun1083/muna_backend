package com.life.muna.product.dto;

import com.life.muna.product.domain.enums.ChatStatus;
import com.life.muna.user.dto.OtherUserProfile;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ReqUserProfile extends OtherUserProfile {
    private String reqContent;
    private ChatStatus chatStatus;
    private String insertDate;
    private String updateDate;

    @Builder
    public ReqUserProfile(String userNickname, byte[] userProfileImage, String location, String reqContent, ChatStatus chatStatus, String insertDate, String updateDate) {
        super(userNickname, userProfileImage, location);
        this.reqContent = reqContent;
        this.chatStatus = chatStatus;
        this.insertDate = insertDate;
        this.updateDate = updateDate;
    }

}
