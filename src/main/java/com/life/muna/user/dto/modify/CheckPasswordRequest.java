package com.life.muna.user.dto.modify;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
public class CheckPasswordRequest {
    @ApiModelProperty(example = "test1234!", required = true)
    private String password;

    @Override
    public String toString() {
        return "CheckPassword {" +
                ", password='" + password + '\'' +
                '}';
    }
}
