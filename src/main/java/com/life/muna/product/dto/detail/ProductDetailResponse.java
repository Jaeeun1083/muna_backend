package com.life.muna.product.dto.detail;

import com.life.muna.product.domain.enums.ProductStatus;
import com.life.muna.user.domain.User;
import com.life.muna.user.dto.profile.OtherUserProfile;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.life.muna.common.util.TimeConverter.convertDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductDetailResponse extends OtherUserProfile {
    private long productCode;
    private long userCode;
    private String content;
    private String imageLink;
    private int imageCnt;
    private int likes;
    private int views;
    private String location;
    private String category;
    private String title;
    private String thumbnail;
    private ProductStatus productStatus;
    private int mcoin;
    private int reqCnt;
    private String insertDate;
    private String updateDate;
    private Boolean isMyProduct;

    private ProductDetailResponse(long productCode, long userCode, String content, String imageLink, int imageCnt, int likes, int views, String location, String category, String title, String thumbnail, ProductStatus productStatus, int mcoin, int reqCnt, LocalDateTime insertDate, LocalDateTime updateDate) {
        this.productCode = productCode;
        this.userCode = userCode;
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
        this.insertDate = convertDate(insertDate);
        this.updateDate = convertDate(updateDate);
    }

    public void setSellerData(User user, boolean isMe) {
        this.userNickname = user.getNickname();
        this.userProfileImage = user.getProfileImage();
        this.userLevel = user.getUserLevel();
        this.isMyProduct = isMe;
    }

}
