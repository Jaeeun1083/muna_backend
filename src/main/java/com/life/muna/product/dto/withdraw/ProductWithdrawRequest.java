package com.life.muna.product.dto.withdraw;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class ProductWithdrawRequest {

    @ApiModelProperty(example = "1", required = true)
    @NotNull(message = "상품 코드는 필수 입력 값 입니다.")
    private Long productCode;

    @Override
    public String toString() {
        return "ProductDelete {" +
                ", productCode=" + productCode +
                '}';
    }
}
