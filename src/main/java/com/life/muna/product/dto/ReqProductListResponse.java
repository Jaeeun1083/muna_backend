package com.life.muna.product.dto;

import com.life.muna.product.domain.Product;
import com.life.muna.product.domain.enums.Category;
import lombok.Builder;
import lombok.Getter;

import static com.life.muna.common.util.TimeConverter.convert;

@Getter
public class ReqProductListResponse {
    private String title;
    private byte[] thumbnail;
    private Category category;
    private Integer mcoin;
    private boolean productStatus;
    private String insertDate;
    private String updateDate;

    @Builder
    public ReqProductListResponse(String title, byte[] thumbnail, Category category, Integer mcoin, boolean productStatus, String insertDate, String updateDate) {
        this.title = title;
        this.thumbnail = thumbnail;
        this.category = category;
        this.mcoin = mcoin;
        this.productStatus = productStatus;
        this.insertDate = insertDate;
        this.updateDate = updateDate;
    }

    public static ReqProductListResponse of(Product product) {
        return ReqProductListResponse.builder()
                .title(product.getTitle())
                .thumbnail(product.getThumbnail())
                .category(product.getCategory())
                .mcoin(product.getMcoin())
                .productStatus(product.getProductStatus())
                .insertDate(convert(product.getInsertDate()))
                .updateDate(convert(product.getUpdateDate()))
                .build();
    }

}
