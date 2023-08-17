package com.life.muna.user.dto.find;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
public class FindPasswordRequest {
    @ApiModelProperty(example = "01012345678", required = true)
    private String phone;

    @Override
    public String toString() {
        return "ResetPassword {" +
                "phone=" + phone +
                '}';
    }

}
