package com.life.muna.product.dto;

import com.life.muna.common.util.Enum;
import com.life.muna.product.domain.Category;
import com.life.muna.product.domain.LocationRange;
import com.life.muna.product.domain.McoinSort;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ProductListRequest {
    @ApiModelProperty(example = "25", required = true)
    @NotNull(message = "유저 코드는 필수 입력 값 입니다.")
    private Long userCode;

    @ApiModelProperty(example = "0", required = true)
    @NotNull(message = "상품 시작 코드는 필수 입력 값 입니다.")
    private Long startProductCode;

    @ApiModelProperty(example = "30", required = true)
    private Integer productDataCnt;

    @ApiModelProperty(example = "1112065")
    private Integer locationCode;

    @ApiModelProperty(example = "DONG", required = true)
    @Enum(enumClass = LocationRange.class, message = "위치 범위 지정 코드가 올바르지 않습니다.")
    private String locationRange;

    @ApiModelProperty(example = "ASC", required = true)
    @Enum(enumClass = McoinSort.class, message = "가격 정렬 코드가 올바르지 않습니다.")
    private String mcoinSort;

    @ApiModelProperty(example = "ALL", required = true)
    @Enum(enumClass = Category.class, message = "카테고리 코드가 올바르지 않습니다.")
    private String category;

    @ApiModelProperty(example = "상품1")
    private String searchKeyword;

    @ApiModelProperty(example = "true", required = true)
    private boolean productStatus;

    public void setLocationCode(Integer locationCode) {
        this.locationCode = locationCode;
    }

}
