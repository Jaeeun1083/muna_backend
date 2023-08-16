package com.life.muna.user.domain;

import com.life.muna.user.domain.enums.LoginType;
import com.life.muna.user.domain.enums.UserLevel;
import com.life.muna.user.domain.enums.UserStatus;
import lombok.Builder;
import lombok.Getter;
import org.springframework.util.ResourceUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

@Getter
public class User {
    private Long userCode;
    private String email;
    private String password;
    private String nickname;
    private LoginType loginType;
    private UserLevel userLevel;
    private Integer reqCnt;
    private Integer cashedReqCnt;
    private byte[] profileImage;
    private String fcmToken;
    private String phone;
    private Long locationDongCd;
    private Boolean notiChat;
    private Boolean notiReq;
    private LocalDateTime insertDate;
    private LocalDateTime updateDate;
    private UserStatus userStatus;

    @Builder
    private User(Long userCode, String email, String password, String nickname, LoginType loginType, UserLevel userLevel, Integer reqCnt, Integer cashedReqCnt, byte[] profileImage, String fcmToken, String phone, Long locationDongCd, boolean notiChat, boolean notiReq, LocalDateTime insertDate, LocalDateTime updateDate, UserStatus userStatus) {
        this.userCode = userCode;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.loginType = loginType;
        this.userLevel = userLevel;
        this.reqCnt = reqCnt;
        this.cashedReqCnt = cashedReqCnt;
        this.profileImage = profileImage;
        this.fcmToken = fcmToken;
        this.phone = phone;
        this.locationDongCd = locationDongCd;
        this.notiChat = notiChat;
        this.notiReq = notiReq;
        this.insertDate = insertDate;
        this.updateDate = updateDate;
        this.userStatus = userStatus;
    }

    public static byte[] createDefaultImage(String defaultImage) {
        byte[] imageInByte;
        try {
            File file = ResourceUtils.getFile(defaultImage);
            BufferedImage originalImage = ImageIO.read(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(originalImage, "jpg", baos);
            baos.flush();

            imageInByte = baos.toByteArray();
            return imageInByte;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

}
