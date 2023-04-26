package com.life.muna.product.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ReqReceivedResponse {
    private ProductRegiListResponse product;
    private List<ReqUserProfile> reqUserProfileList;

    @Builder
    public ReqReceivedResponse(ProductRegiListResponse product, List<ReqUserProfile> reqUserProfileList) {
        this.product = product;
        this.reqUserProfileList = reqUserProfileList;
    }

}
