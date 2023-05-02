package com.life.muna.product.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ReqReceivedResponse {
    private ProductHistoryResponse product;
    private List<ReqUserProfile> reqUserProfileList;

    @Builder
    public ReqReceivedResponse(ProductHistoryResponse product, List<ReqUserProfile> reqUserProfileList) {
        this.product = product;
        this.reqUserProfileList = reqUserProfileList;
    }

}
