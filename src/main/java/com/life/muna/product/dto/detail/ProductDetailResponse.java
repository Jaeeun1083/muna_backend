package com.life.muna.product.dto.detail;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.life.muna.product.domain.enums.ProductStatus;
import com.life.muna.user.domain.User;
import com.life.muna.user.domain.enums.UserLevel;
import com.life.muna.user.dto.profile.OtherUserProfile;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

import static com.life.muna.common.util.TimeConverter.convert;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductDetailResponse extends OtherUserProfile {
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
    private ProductStatus productStatus;
    private Integer mcoin;
    private Integer reqCnt;
    private String insertDate;
    private String updateDate;
    @JsonIgnore
    private Long userCode;
    private Boolean isMyProduct;

    private ProductDetailResponse(Long productCode, String content, String imageLink, Integer imageCnt, Integer likes, Integer views, String location, String category, String title, String thumbnail, ProductStatus productStatus, Integer mcoin, Integer reqCnt, Date insertDate, Date updateDate, Long userCode) {
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

    public void setSellerData(User user, boolean isMe) {
        this.userNickname = user.getNickname();
        this.userProfileImage = user.getProfileImage();
        this.userLevel = user.getUserLevel();
        this.isMyProduct = isMe;
    }

}
