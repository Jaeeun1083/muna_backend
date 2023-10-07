package com.life.muna.product.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
public class ProductHistory {
    private Long productCode;
    private Long sellerCode;
    private Long buyerCode;
    private Integer mcoin;
    private Date insertDate;
    private Date updateDate;

    @Builder
    private ProductHistory(Long productCode, Long sellerCode, Long buyerCode, Integer mcoin, Date insertDate, Date updateDate) {
        this.productCode = productCode;
        this.sellerCode = sellerCode;
        this.buyerCode = buyerCode;
        this.mcoin = mcoin;
        this.insertDate = insertDate;
        this.updateDate = updateDate;
    }

}
