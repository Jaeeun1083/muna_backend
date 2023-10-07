package com.life.muna.product.dto.history;

import com.life.muna.product.domain.Product;
import com.life.muna.product.domain.enums.Category;
import com.life.muna.product.domain.enums.ProductStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductHistoryResponse {
    private Long productCode;
    private String title;
    private byte[] thumbnail;
    private Category category;
    private Integer reqCnt;
    private Integer mcoin;
    private ProductStatus productStatus;

    @Builder
    public ProductHistoryResponse(Long productCode, String title, byte[] thumbnail, Category category, Integer reqCnt, Integer mcoin, ProductStatus productStatus) {
        this.productCode = productCode;
        this.title = title;
        this.thumbnail = thumbnail;
        this.category = category;
        this.reqCnt = reqCnt;
        this.mcoin = mcoin;
        this.productStatus = productStatus;
    }

    public static ProductHistoryResponse of(Product product, int reqCnt) {
        return ProductHistoryResponse.builder()
                .productCode(product.getProductCode())
                .title(product.getTitle())
                .thumbnail(product.getThumbnail())
                .category(product.getCategory())
                .reqCnt(reqCnt)
                .mcoin(product.getMcoin())
                .productStatus(product.getProductStatus())
                .build();
    }

    public static ProductHistoryResponse of(Product product) {
        return ProductHistoryResponse.builder()
                .productCode(product.getProductCode())
                .title(product.getTitle())
                .thumbnail(product.getThumbnail())
                .category(product.getCategory())
                .mcoin(product.getMcoin())
                .productStatus(product.getProductStatus())
                .build();
    }

}
