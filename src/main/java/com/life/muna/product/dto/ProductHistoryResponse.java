package com.life.muna.product.dto;

import com.life.muna.product.domain.Product;
import com.life.muna.product.domain.enums.Category;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductHistoryResponse {
    private Long productCode;
    private String title;
    private byte[] thumbnail;
    private Category category;
    private Integer requestCount;
    private Integer mcoin;
    private boolean productStatus;

    @Builder
    public ProductHistoryResponse(Long productCode, String title, byte[] thumbnail, Category category, Integer requestCount, Integer mcoin, boolean productStatus) {
        this.productCode = productCode;
        this.title = title;
        this.thumbnail = thumbnail;
        this.category = category;
        this.requestCount = requestCount;
        this.mcoin = mcoin;
        this.productStatus = productStatus;
    }

    public static ProductHistoryResponse of(Product product, int requestCount) {
        return ProductHistoryResponse.builder()
                .productCode(product.getProductCode())
                .title(product.getTitle())
                .thumbnail(product.getThumbnail())
                .requestCount(requestCount)
                .mcoin(product.getMcoin())
                .productStatus(product.getProductStatus())
                .build();
    }

}
