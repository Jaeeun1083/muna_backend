package com.life.muna.product.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.life.muna.user.domain.User;
import com.life.muna.user.domain.enums.UserLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

import static com.life.muna.common.util.TimeConverter.convert;

@Getter
public class ProductDetailResponse {
    private Long productCode;
    private String content;
    private String imageLink;
    private Integer imageCnt;
    private Integer likes;
    private Integer views;
    private String location;
    private String category;
    private String title;
    private String thumbnail;
    private Boolean productStatus;
    private Integer mcoin;
    private Integer reqCnt;
    private String insertDate;
    private String updateDate;
    @JsonIgnore
    private Long userCode;
    private String userNickname;
    private byte[] userProfileImage;
    private UserLevel userLevel;
    private Boolean isRequested;
    private Boolean isLiked;

    @Builder
    public ProductDetailResponse(Long productCode, String content, String imageLink, Integer imageCnt, Integer likes, Integer views, String location, String category, String title, String thumbnail, boolean productStatus, Integer mcoin, Integer reqCnt, Date insertDate, Date updateDate, Long userCode) {
        this.productCode = productCode;
        this.content = content;
        this.imageLink = imageLink;
        this.imageCnt = imageCnt;
        this.likes = likes;
        this.views = views;
        this.location = location;
        this.category = category;
        this.title = title;
        this.thumbnail = thumbnail;
        this.productStatus = productStatus;
        this.mcoin = mcoin;
        this.reqCnt = reqCnt;
        this.insertDate = convert(insertDate);
        this.updateDate = convert(insertDate);
        this.userCode = userCode;
    }

    public ProductDetailResponse setSellerData(User user) {
        this.userProfileImage = user.getProfileImage();
        this.userNickname = user.getNickname();
        this.userLevel = user.getUserLevel();
        return this;
    }

    public ProductDetailResponse setMyInformation(boolean isRequested, boolean isLiked) {
        this.isRequested = isRequested;
        this.isLiked = isLiked;
        return this;
    }

}
