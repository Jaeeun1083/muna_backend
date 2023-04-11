package com.life.muna.product.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
public class ProductDetail {
    private Long productCode;
    private Long userCode;
    private String content;
    private String imageLink;
    private Integer imageCnt;
    private Integer likes;
    private Integer views;
    private Date insertDate;
    private Date updateDate;

    @Builder
    public ProductDetail(Long productCode, Long userCode, String content, String imageLink, Integer imageCnt, Integer likes, Integer views, Date insertDate, Date updateDate) {
        this.productCode = productCode;
        this.userCode = userCode;
        this.content = content;
        this.imageLink = imageLink;
        this.imageCnt = imageCnt;
        this.likes = likes;
        this.views = views;
        this.insertDate = insertDate;
        this.updateDate = updateDate;
    }

}
