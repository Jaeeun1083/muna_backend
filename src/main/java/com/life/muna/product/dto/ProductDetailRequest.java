package com.life.muna.product.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class ProductDetailRequest {
    @ApiModelProperty(example = "25", required = true)
    @NotNull(message = "유저 코드는 필수 입력 값 입니다.")
    private Long userCode;
}
