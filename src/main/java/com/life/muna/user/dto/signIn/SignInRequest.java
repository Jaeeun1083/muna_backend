package com.life.muna.user.dto.signIn;

import com.life.muna.common.util.EnumValue;
import com.life.muna.user.domain.enums.LoginType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
public class SignInRequest {
    @ApiModelProperty(example = "muna@munaApp.com", required = true)
    @NotBlank(message = "이메일은 필수 입력 값 입니다.")
    @Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$",
            message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @ApiModelProperty(example = "test1234!")
    private String password;

    @ApiModelProperty(example = "EMAIL", required = true)
    @EnumValue(enumClass = LoginType.class, message = "로그인 타입이 올바르지 않습니다.")
    private String loginType;

}
