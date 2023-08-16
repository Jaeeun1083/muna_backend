package com.life.muna.user.dto.signUp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class VerifyTempKeyRequest {
    @ApiModelProperty(example = "01011111111", required = true)
    @NotNull(message = "핸드폰 번호는 필수 입력 값 입니다.")
    private String phone;

    private int tempKey;

    @Override
    public String toString() {
        return "VerifyTempKey {" +
                "phone='" + phone + '\'' +
                ", tempKey='" + tempKey + '\'' +
                '}';
    }

}
