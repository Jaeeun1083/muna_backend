package com.life.muna.like.dto;

import com.life.muna.product.domain.Product;
import lombok.Builder;
import lombok.Getter;

import static com.life.muna.common.util.TimeConverter.convert;

@Getter
public class ProductLikeListResponse {
    private Long productCode;
    private String title;
    private byte[] thumbnail;
    private Integer reqCnt;
    private Integer mcoin;
    private boolean productStatus;
    private String insertDate;
    private String updateDate;

    @Builder
    private ProductLikeListResponse(Long productCode, String title, byte[] thumbnail, Integer reqCnt, Integer mcoin, boolean productStatus, String insertDate, String updateDate) {
        this.productCode = productCode;
        this.title = title;
        this.thumbnail = thumbnail;
        this.reqCnt = reqCnt;
        this.mcoin = mcoin;
        this.productStatus = productStatus;
        this.insertDate = insertDate;
        this.updateDate = updateDate;
    }

    public static ProductLikeListResponse of(Product product) {
        return ProductLikeListResponse.builder()
                .productCode(product.getProductCode())
                .title(product.getTitle())
                .thumbnail(product.getThumbnail())
                .reqCnt(product.getReqCnt())
                .mcoin(product.getMcoin())
                .productStatus(product.getProductStatus())
                .insertDate(convert(product.getInsertDate()))
                .updateDate(convert(product.getUpdateDate()))
                .build();
    }

}
