package com.life.muna.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
public class DuplicateEmailRequest {
    @ApiModelProperty(example = "muna@munaApp.com", required = true)
    @NotBlank(message = "이메일은 필수 입력 값 입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;
}
