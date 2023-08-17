package com.life.muna.user.dto.modify;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
public class ModifyPasswordRequest {
    @ApiModelProperty(example = "test1234!", required = true)
    private String before;

    @ApiModelProperty(example = "test12341234!", required = true)
    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#\\\\$%^&*]).{8,20}$",
            message = "비밀번호는 영문과 숫자또는 특수문자 조합으로 8자 ~ 20자의 비밀번호여야 합니다.")
    private String after;

    @Override
    public String toString() {
        return "ModifyPassword {" +
                ", before='" + before + '\'' +
                ", after='" + after + '\'' +
                '}';
    }
}
