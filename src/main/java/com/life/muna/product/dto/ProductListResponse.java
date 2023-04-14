package com.life.muna.product.dto;

import com.life.muna.product.domain.enums.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ProductListResponse {
    private Long productCode;
    private String location;
    private Category category;
    private String title;
    private byte[] thumbnail;
    private boolean productStatus;
    private Integer mcoin;
    private Integer reqCnt;
    private Date insertDate;
    private Date updateDate;

    @Builder
    public ProductListResponse(Long productCode, String location, Category category, String title, byte[] thumbnail, boolean productStatus, Integer mcoin, Integer reqCnt, Date insertDate, Date updateDate) {
        this.productCode = productCode;
        this.location = location;
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
