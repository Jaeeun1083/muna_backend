package com.life.muna.product.domain;

import com.life.muna.product.domain.enums.Category;
import com.life.muna.product.domain.enums.ProductStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
public class Product {
    private Long productCode;
    private Long userCode;
    private Long locationDongCd;
    private Category category;
    private String title;
    private byte[] thumbnail;
    private ProductStatus productStatus;
    private Integer mcoin;
    private Integer reqCnt;
    private LocalDateTime insertDate;
    private LocalDateTime updateDate;

    @Builder
    private Product(Long productCode, Long userCode, Long locationDongCd, Category category, String title, byte[] thumbnail, ProductStatus productStatus, Integer mcoin, Integer reqCnt, LocalDateTime insertDate, LocalDateTime updateDate) {
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
