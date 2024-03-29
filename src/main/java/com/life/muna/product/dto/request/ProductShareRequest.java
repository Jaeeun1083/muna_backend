package com.life.muna.product.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.life.muna.productReq.domain.ReqProduct;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
public class ProductShareRequest {

    @ApiModelProperty(example = "1", required = true)
    @NotNull(message = "상품 코드는 필수 입력 값 입니다.")
    private Long productCode;

    @ApiModelProperty(example = "나눔 요청 부탁드립니다.", required = true)
    @NotNull(message = "요청 메시지는 필수 입력 값 입니다.")
    @Size(max = 50, message = "요청 메시지는 50자 미만으로 작성해야합니다.")
    private String requestContent;


    public ReqProduct toEntity(ProductShareRequest productShareRequest, Long requesterCode, Long sellerCode, boolean isCashedReq) {
        return ReqProduct.builder()
                .productCode(productShareRequest.getProductCode())
                .sellerCode(sellerCode)
                .requesterCode(requesterCode)
                .cashedReq(isCashedReq)
                .reqContent(productShareRequest.getRequestContent())
                .reqRead(false)
                .build();
    }

    @Override
    public String toString() {
        return "ProductShare {" +
                "productCode=" + productCode +
                ", requestContent='" + requestContent + '\'' +
                '}';
    }
}
