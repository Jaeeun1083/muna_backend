package com.life.muna.productReq.dto.profile;

import com.life.muna.productReq.domain.ReqProduct;
import com.life.muna.productReq.domain.enums.ReqStatus;
import com.life.muna.user.domain.User;
import com.life.muna.user.domain.enums.UserLevel;
import com.life.muna.user.dto.profile.OtherUserProfile;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.life.muna.common.util.TimeConverter.convertDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class ReqUserProfile extends OtherUserProfile {
    private long productReqCode;
    private String roomCode;
    private String location;
    private String reqContent;
    private boolean reqRead;
    private ReqStatus reqStatus;
    private String insertDate;
    private String updateDate;

    @Builder
    private ReqUserProfile(long productReqCode, String roomCode, String userNickname, UserLevel userLevel, byte[] userProfileImage, String location, String reqContent, boolean reqRead, ReqStatus reqStatus, String insertDate, String updateDate) {
        super(userNickname, userProfileImage, userLevel);
        this.productReqCode = productReqCode;
        this.roomCode = roomCode;
        this.location = location;
        this.reqContent = reqContent;
        this.reqRead = reqRead;
        this.reqStatus = reqStatus;
        this.insertDate = insertDate;
        this.updateDate = updateDate;
    }

    public static ReqUserProfile of (User user, ReqProduct reqProduct, String roomCode, String locationName) {
        return ReqUserProfile.builder()
                .productReqCode(reqProduct.getProductReqCode())
                .roomCode(roomCode)
                .userNickname(user.getNickname())
                .userLevel(user.getUserLevel())
                .userProfileImage(user.getProfileImage())
                .location(locationName)
                .reqContent(reqProduct.getReqContent())
                .reqRead(reqProduct.getReqRead())
                .reqStatus(reqProduct.getReqStatus())
                .insertDate(convertDate(reqProduct.getInsertDate()))
                .updateDate(convertDate(reqProduct.getUpdateDate()))
                .build();
    }

}
