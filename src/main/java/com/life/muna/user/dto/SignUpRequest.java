package com.life.muna.user.dto;

import com.life.muna.auth.util.PasswordEncoder;
import com.life.muna.user.domain.User;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
public class SignUpRequest {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Length(min=8, max = 20)
    private String password;
    private String nickname;
    private String loginType;
    private String profileImage;
    private int phone;

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
