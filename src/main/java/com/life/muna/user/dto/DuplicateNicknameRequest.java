package com.life.muna.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
public class DuplicateNicknameRequest {
    @ApiModelProperty(example = "마루도키", required = true)
    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    @Pattern(regexp = "^[0-9a-zA-Zㄱ-ㅎ가-힣]*$",
            message = "닉네임은 영문 또는 한글 또는 숫자이어야 합니다.")
    private String nickname;
}
