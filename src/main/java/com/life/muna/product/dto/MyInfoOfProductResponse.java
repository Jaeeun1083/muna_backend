package com.life.muna.product.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MyInfoOfProductResponse {
    private Boolean isRequested;
    private Boolean isLiked;

    @Builder
    private MyInfoOfProductResponse(Boolean isRequested, Boolean isLiked) {
        this.isRequested = isRequested;
        this.isLiked = isLiked;
    }

    public static MyInfoOfProductResponse setMyInformation(boolean isRequested, boolean isLiked) {
        return MyInfoOfProductResponse.builder()
                .isRequested(isRequested)
                .isLiked(isLiked)
                .build();
    }

}
