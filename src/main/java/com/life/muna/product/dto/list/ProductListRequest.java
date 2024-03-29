package com.life.muna.product.dto.list;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.life.muna.common.util.EnumValue;
import com.life.muna.product.domain.enums.Category;
import com.life.muna.product.domain.enums.LocationRange;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ProductListRequest {
    @ApiModelProperty(example = "1", required = true)
    @NotNull(message = "페이지 넘버는 필수 입력 값 입니다.")
    @Min(value = 1, message = "페이지는 1 이상의 값이어야 합니다.")
    private Integer page;

    @ApiModelProperty(example = "0")
    private Long maxProductCode;

    @JsonIgnore
    private int pageSize;

    @JsonIgnore
    private int offset;

    @JsonIgnore
    private Long locationCode;

    @ApiModelProperty(example = "L000", required = true)
    @EnumValue(enumClass = LocationRange.class, message = "위치 범위 코드가 올바르지 않습니다")
    private String locationRange;

    @ApiModelProperty(example = "true")
    private Boolean mcoinSort;

    @ApiModelProperty(example = "C000", required = true)
    @EnumValue(enumClass = Category.class, message = "카테고리 코드가 올바르지 않습니다")
    private String category;

    @ApiModelProperty(example = "1")
    private String searchKeyword;

    @ApiModelProperty(example = "true", required = true)
    @NotNull(message = "상품 판매 여부는 필수입니다.")
    private Boolean productStatus;

    @ApiModelProperty(hidden = true)
    public void setPageInfo(int pageSize) {
        this.pageSize = pageSize;
        this.offset = (this.page -1) * pageSize;
    }

}
