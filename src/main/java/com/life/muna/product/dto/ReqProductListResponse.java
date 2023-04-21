package com.life.muna.product.dto;

import com.life.muna.product.domain.Product;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ReqProductListResponse {
    private Long productCode;
    private String title;
    private byte[] thumbnail;
    private Integer requestCount;
    private Integer mcoin;
    private boolean productStatus;

    @Builder
    public ReqProductListResponse(Long productCode, String title, byte[] thumbnail, Integer requestCount, Integer mcoin, boolean productStatus) {
        this.productCode = productCode;
        this.title = title;
        this.thumbnail = thumbnail;
        this.requestCount = requestCount;
        this.mcoin = mcoin;
        this.productStatus = productStatus;
    }

    public static ReqProductListResponse of(Product product, int requestCount) {
        return ReqProductListResponse.builder()
                .productCode(product.getProductCode())
                .title(product.getTitle())
                .thumbnail(product.getThumbnail())
                .requestCount(requestCount)
                .mcoin(product.getMcoin())
                .productStatus(product.getProductStatus())
                .build();
    }

}
