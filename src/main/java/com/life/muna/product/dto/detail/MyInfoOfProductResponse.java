package com.life.muna.product.dto.detail;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MyInfoOfProductResponse {
    private boolean isRequested;
    private boolean isLiked;

    @Builder
    private MyInfoOfProductResponse(boolean isRequested, boolean isLiked) {
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
