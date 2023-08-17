package com.life.muna.user.dto.fcm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class FcmTokenRequest {

    @ApiModelProperty(example = "", required = true)
    @NotBlank(message = "Token은 필수 입력 값입니다.")
    private String fcmToken;

    @Override
    public String toString() {
        return "{" +
                ", fcmToken='" + fcmToken + '\'' +
                '}';
    }
}
