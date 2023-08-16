package com.life.muna.user.dto.reissue;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
public class ReissueRequest {
    @ApiModelProperty(example = "muna@munaApp.com", required = true)
    @NotBlank(message = "이메일은 필수 입력 값 입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @ApiModelProperty(example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkZXZwc2dAa2FrYW8uY29tIiwiaWF0IjoxNjc5NzEyNzcxLCJleHAiOjE2Nzk3NDg3NzF9.GpHapkeEpHDc8mu1SItrTXeaSRXBlr5ZZ0p8JyuVHIc", required = true)
    @NotBlank(message = "refreshToken은 필수 입력 값입니다.")
    private String refreshToken;
}
