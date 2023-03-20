package com.life.muna.user.dto;

import com.life.muna.auth.util.PasswordEncoder;
import com.life.muna.user.domain.User;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
public class SignUpRequest {
    @NotBlank(message = "이메일은 필수 입력 값 입니다.")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-zA-Z])[0-9a-zA-Z]{8,20}",
            message = "비밀번호는 영문과 숫자 조합으로 8자 ~ 20자의 비밀번호여야 합니다.")
    private String password;

    private String nickname;

    private String loginType;

    private String profileImage;

    @NotBlank(message = "휴대폰번호를 입력해주세요.")
    @Pattern(regexp = "[0-9]{10,11}", message = "10~11자리의 숫자만 입력가능합니다")
    private String phone;

    public User toEntity(SignUpRequest signUpRequest, PasswordEncoder passwordEncoder) {
        return User.builder()
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encrypt(signUpRequest.getPassword()))
                .nickname(signUpRequest.getNickname())
                .loginType(signUpRequest.getLoginType())
                .profileImage(signUpRequest.getProfileImage())
                .phone(signUpRequest.getPhone())
                .build();
    }

}
