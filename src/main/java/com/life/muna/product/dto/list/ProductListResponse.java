package com.life.muna.product.dto.list;

import com.life.muna.product.domain.enums.Category;
import com.life.muna.product.domain.enums.ProductStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

import static com.life.muna.common.util.TimeConverter.convertDate;

@Getter
@Setter
public class ProductListResponse {
    private Long productCode;
    private String location;
    private Category category;
    private String title;
    private byte[] thumbnail;
    private ProductStatus productStatus;
    private Integer mcoin;
    private Integer reqCnt;
    private String insertDate;
    private String updateDate;

    @Builder
    public ProductListResponse(Long productCode, String location, Category category, String title, byte[] thumbnail, ProductStatus productStatus, Integer mcoin, Integer reqCnt, Date insertDate, Date updateDate) {
        this.productCode = productCode;
        this.location = location;
        this.category = category;
        this.title = title;
        this.thumbnail = thumbnail;
        this.productStatus = productStatus;
        this.mcoin = mcoin;
        this.reqCnt = reqCnt;
        this.insertDate = convertDate(insertDate);
        this.updateDate = convertDate(updateDate);
    }

}
