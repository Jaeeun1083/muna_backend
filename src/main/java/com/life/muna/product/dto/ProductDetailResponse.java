package com.life.muna.product.dto;

import com.life.muna.product.domain.ProductDetail;
import com.life.muna.user.domain.User;
import com.life.muna.user.domain.UserLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
public class ProductDetailResponse {
    private Long productCode;
    private String content;
    private String imageLink;
    private Integer likes;
    private Integer views;
    private byte[] userProfileImage;
    private String userNickname;
    private UserLevel userLevel;
    private Date insertDate;
    private Date updateDate;

    @Builder
    private ProductDetailResponse(Long productCode, String content, String imageLink, Integer likes, Integer views, byte[] userProfileImage, String userNickname, UserLevel userLevel, Date insertDate, Date updateDate) {
        this.productCode = productCode;
        this.content = content;
        this.imageLink = imageLink;
        this.likes = likes;
        this.views = views;
        this.userProfileImage = userProfileImage;
        this.userNickname = userNickname;
        this.userLevel = userLevel;
        this.insertDate = insertDate;
        this.updateDate = updateDate;
    }

    public static ProductDetailResponse to(User user, ProductDetail productDetail) {
        return ProductDetailResponse.builder()
                .productCode(productDetail.getProductCode())
                .content(productDetail.getContent())
                .imageLink(productDetail.getImageLink())
                .likes(productDetail.getLikes())
                .views(productDetail.getViews())
                .userProfileImage(user.getProfileImage())
                .userNickname(user.getNickname())
                .userLevel(user.getUserLevel())
                .insertDate(productDetail.getInsertDate())
                .updateDate(productDetail.getUpdateDate())
                .build();
    }

}
