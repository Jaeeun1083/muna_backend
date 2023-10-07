package com.life.muna.productReq.dto;

import com.life.muna.product.domain.Product;
import com.life.muna.product.domain.enums.ProductStatus;
import com.life.muna.productReq.domain.enums.ReqStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ReqProductRequestedListResponse {
    private Long productCode;
    private String title;
    private byte[] thumbnail;
    private Integer reqCnt;
    private Integer mcoin;
    private ProductStatus productStatus;
    private ReqStatus reqStatus;

    @Builder
    private ReqProductRequestedListResponse(Long productCode, String title, byte[] thumbnail, Integer reqCnt, Integer mcoin, ProductStatus productStatus, ReqStatus reqStatus) {
        this.productCode = productCode;
        this.title = title;
        this.thumbnail = thumbnail;
        this.reqCnt = reqCnt;
        this.mcoin = mcoin;
        this.productStatus = productStatus;
        this.reqStatus = reqStatus;
    }

    public static ReqProductRequestedListResponse of(Product product, ReqStatus reqStatus) {
        return ReqProductRequestedListResponse.builder()
                .productCode(product.getProductCode())
                .title(product.getTitle())
                .thumbnail(product.getThumbnail())
                .reqCnt(product.getReqCnt())
                .mcoin(product.getMcoin())
                .productStatus(product.getProductStatus())
                .reqStatus(reqStatus)
                .build();
    }
}
