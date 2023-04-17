package com.life.muna.product.domain;

import com.life.muna.product.domain.enums.Category;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
public class Product {
    private Long productCode;
    private Long userCode;
    private Long locationDongCd;
    private Category category;
    private String title;
    private byte[] thumbnail;
    private Boolean productStatus;
    private Integer mcoin;
    private Integer reqCnt;
    private Date insertDate;
    private Date updateDate;

    @Builder
    public Product(Long productCode, Long userCode, Long locationDongCd, Category category, String title, byte[] thumbnail, boolean productStatus, Integer mcoin, Integer reqCnt, Date insertDate, Date updateDate) {
        this.productCode = productCode;
        this.userCode = userCode;
        this.locationDongCd = locationDongCd;
        this.category = category;
        this.title = title;
        this.thumbnail = thumbnail;
        this.productStatus = productStatus;
        this.mcoin = mcoin;
        this.reqCnt = reqCnt;
        this.insertDate = insertDate;
        this.updateDate = updateDate;
    }

}
