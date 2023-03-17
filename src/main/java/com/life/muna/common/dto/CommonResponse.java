package com.life.muna.common.dto;

import lombok.Builder;

public record CommonResponse<T>(int statusCode, T data, String message) {

    @Builder
    public CommonResponse {
    }

}
