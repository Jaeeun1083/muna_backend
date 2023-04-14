package com.life.muna.user.dto;

import com.life.muna.auth.util.PasswordEncoder;
import com.life.muna.common.util.EnumValue;
import com.life.muna.user.domain.enums.LoginType;
import com.life.muna.user.domain.User;
import com.life.muna.user.domain.enums.UserLevel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
public class SignUpRequest {
    @ApiModelProperty(example = "muna@munaApp.com", required = true)
    @NotBlank(message = "이메일은 필수 입력 값 입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @ApiModelProperty(example = "test1234", required = true)
    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#\\\\$%^&*]).{8,20}$",
            message = "비밀번호는 영문과 숫자또는 특수문자 조합으로 8자 ~ 20자의 비밀번호여야 합니다.")
    private String password;

    @ApiModelProperty(example = "마루도키", required = true)
    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    @Pattern(regexp = "^[0-9a-zA-Zㄱ-ㅎ가-힣]*$",
            message = "닉네임은 영문 또는 한글 또는 숫자이어야 합니다.")
    private String nickname;

    @ApiModelProperty(example = "EMAIL", required = true)
    @EnumValue(enumClass = LoginType.class, message = "로그인 타입이 올바르지 않습니다.")
    private String loginType;

    @ApiModelProperty(hidden = true)
    private byte[] profileImage;

    @ApiModelProperty(example = "01012345678", required = true)
    @NotBlank(message = "휴대폰 번호를 입력해주세요.")
    @Pattern(regexp = "[0-9]{10,11}", message = "10~11자리의 숫자만 입력가능합니다")
    private String phone;

    public User toEntity(SignUpRequest signUpRequest, PasswordEncoder passwordEncoder) {
        return User.builder()
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encrypt(signUpRequest.getPassword()))
                .nickname(signUpRequest.getNickname())
                .loginType(LoginType.fromTypeName(signUpRequest.getLoginType()))
                .userLevel(UserLevel.BASIC)
                .profileImage(signUpRequest.getProfileImage())
                .phone(signUpRequest.getPhone())
                .build();
    }

    public void setProfileImage(byte[] profileImage) {
        this.profileImage = profileImage;
    }

}
