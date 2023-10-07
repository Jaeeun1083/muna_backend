package com.life.muna.product.dto;

import com.life.muna.product.domain.Product;
import com.life.muna.product.domain.enums.ProductStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ReqProductReceivedListResponse {
    private long productCode;
    private String title;
    private byte[] thumbnail;
    private int reqCnt;
    private int mcoin;
    private ProductStatus productStatus;
    private boolean isRead;

    @Builder
    private ReqProductReceivedListResponse(long productCode, String title, byte[] thumbnail, int reqCnt, int mcoin, ProductStatus productStatus, boolean isRead) {
        this.productCode = productCode;
        this.title = title;
        this.thumbnail = thumbnail;
        this.reqCnt = reqCnt;
        this.mcoin = mcoin;
        this.productStatus = productStatus;
        this.isRead = isRead;
    }

    public static ReqProductReceivedListResponse of(Product product, int reqCnt, boolean isRead) {
        return ReqProductReceivedListResponse.builder()
                .productCode(product.getProductCode())
                .title(product.getTitle())
                .thumbnail(product.getThumbnail())
                .reqCnt(reqCnt)
                .mcoin(product.getMcoin())
                .productStatus(product.getProductStatus())
                .isRead(isRead)
                .build();
    }

}