package com.life.muna.productReq.dto.list;

import com.life.muna.product.domain.Product;
import com.life.muna.product.domain.enums.ProductStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ReqProductReceivedReqListResponse {
    private Long productCode;
    private String title;
    private byte[] thumbnail;
    private Integer reqCnt;
    private Integer mcoin;
    private ProductStatus productStatus;

    @Builder
    private ReqProductReceivedReqListResponse(Long productCode, String title, byte[] thumbnail, Integer reqCnt, Integer mcoin, ProductStatus productStatus) {
        this.productCode = productCode;
        this.title = title;
        this.thumbnail = thumbnail;
        this.reqCnt = reqCnt;
        this.mcoin = mcoin;
        this.productStatus = productStatus;
    }

    public static ReqProductReceivedReqListResponse of(Product product) {
        return ReqProductReceivedReqListResponse.builder()
                .productCode(product.getProductCode())
                .title(product.getTitle())
                .thumbnail(product.getThumbnail())
                .reqCnt(product.getReqCnt())
                .mcoin(product.getMcoin())
                .productStatus(product.getProductStatus())
                .build();
    }
}
