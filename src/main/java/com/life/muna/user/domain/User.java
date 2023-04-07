package com.life.muna.user.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
public class User {
    private Long userCode;
    private String email;
    private String password;
    private String nickname;
    private LoginType loginType;
    private UserLevel userLevel;
    private byte[] profileImage;
    private String firebaseToken;
    private String phone;
    private String address;
    private Date insertDate;
    private Date updateDate;

    @Builder
    public User(Long userCode, String email, String password, String nickname, LoginType loginType, UserLevel userLevel, byte[] profileImage, String firebaseToken, String phone, String address, Date insertDate, Date updateDate) {
        this.userCode = userCode;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.loginType = loginType;
        this.userLevel = userLevel;
        this.profileImage = profileImage;
        this.firebaseToken = firebaseToken;
        this.phone = phone;
        this.address = address;
        this.insertDate = insertDate;
        this.updateDate = updateDate;
    }

}
