package com.life.muna.user.dto.signUp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.constraints.Pattern;

@Getter
public class IssueTempKeyRequest {
    @ApiModelProperty(example = "01011111111", required = true)
    @Pattern(regexp = "[0-9]{10,11}", message = "10~11자리의 숫자만 입력가능합니다")
    private String phone;

    @Override
    public String toString() {
        return "IssueTempKey {" +
                "phone=" + phone + '\'' +
                '}';
    }

}
