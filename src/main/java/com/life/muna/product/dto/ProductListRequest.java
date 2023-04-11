package com.life.muna.product.dto;

import com.life.muna.common.util.EnumValue;
import com.life.muna.product.domain.Category;
import com.life.muna.product.domain.LocationRange;
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
    @NotNull(message = "가져올 상품 개수는 필수 입력 값 입니다.")
    private Integer productDataCnt;

    @ApiModelProperty(example = "서울특별시 은평구 신사1동")
    private String location;

    @ApiModelProperty(hidden = true)
    private Integer locationCode;

    @ApiModelProperty(example = "L000", required = true)
    @EnumValue(enumClass = LocationRange.class, message = "위치 범위 코드가 올바르지 않습니다")
    private String locationRange;

    @ApiModelProperty(example = "true")
    private Boolean mcoinSort;

    @ApiModelProperty(example = "C000", required = true)
    @EnumValue(enumClass = Category.class, message = "카테고리 코드가 올바르지 않습니다")
    private String category;

    @ApiModelProperty(example = "상품1")
    private String searchKeyword;

    @ApiModelProperty(example = "true", required = true)
    @NotNull(message = "상품 판매 여부는 필수입니다.")
    private Boolean productStatus;

}
