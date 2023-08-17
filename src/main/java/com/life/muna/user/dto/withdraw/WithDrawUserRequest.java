package com.life.muna.user.dto.withdraw;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class WithDrawUserRequest {
    @ApiModelProperty(example = "test1234!")
    private String password;

    @Override
    public String toString() {
        return "WithDrawUser {" +
                ", password='" + password + '\'' +
                '}';
    }
}
