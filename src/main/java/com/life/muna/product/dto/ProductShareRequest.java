package com.life.muna.product.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
public class ProductShareRequest {
//    @ApiModelProperty(example = "25", required = true)
//    @NotNull(message = "유저 코드는 필수 입력 값 입니다.")
//    private Long userCode;

    @ApiModelProperty(hidden = true)
    private Long productCode;

    @ApiModelProperty(example = "나눔 요청 부탁드립니다.", required = true)
    @NotNull(message = "요청 메시지는 필수 입력 값 입니다.")
    @Size(max = 50, message = "요청 메시지는 50자 미만으로 작성해야합니다.")
    private String requestContent;

    public void setProductCode(Long productCode) {
        this.productCode = productCode;
    }

}
