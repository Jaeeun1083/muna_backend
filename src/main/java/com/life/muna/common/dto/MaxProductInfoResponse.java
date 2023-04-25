package com.life.muna.common.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MaxProductInfoResponse {
    private Integer maxPage;
    private Long maxCode;

    @Builder
    public MaxProductInfoResponse(Integer maxPage, Long maxCode) {
        this.maxPage = maxPage;
        this.maxCode = maxCode;
    }

}
