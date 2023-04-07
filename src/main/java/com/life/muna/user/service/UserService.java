package com.life.muna.user.service;

import com.life.muna.auth.util.JwtTokenProvider;
import com.life.muna.auth.util.PasswordEncoder;
import com.life.muna.common.error.ErrorCode;
import com.life.muna.common.error.exception.BusinessException;
import com.life.muna.user.domain.User;
import com.life.muna.user.dto.SignInRequest;
import com.life.muna.user.dto.SignInResponse;
import com.life.muna.user.dto.SignUpRequest;
import com.life.muna.user.dto.UserProfileResponse;
import com.life.muna.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final String defaultImage;

    public UserService(UserMapper userMapper, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider, @Value("${image.path}") String imagePath) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.defaultImage = imagePath + "profile.jpg";
    }

    public Boolean signUp(SignUpRequest signUpRequest) {
        boolean isExistsUser = userMapper.existsByEmail(signUpRequest.getEmail());
        if (isExistsUser) throw new BusinessException(ErrorCode.ALREADY_EXISTS_USER);
        if (signUpRequest.getProfileImage() == null || signUpRequest.getProfileImage().length == 0) {
            signUpRequest.setProfileImage(createDefaultImage());
        }
        User user = signUpRequest.toEntity(signUpRequest, passwordEncoder);
        int signUpResult = userMapper.signUp(user);
        return signUpResult == 1;
    }

    public SignInResponse signIn(SignInRequest signInRequest) {
        Optional<User>  userOptional = userMapper.findUserByEmail(signInRequest.getEmail());
        if (userOptional.isEmpty()) throw new BusinessException(ErrorCode.NOT_FOUND_USER);
        User user = userOptional.get();
        checkPassword(user, signInRequest.getPassword());
        return new SignInResponse(user.getUserCode(), jwtTokenProvider.createToken(user.getEmail()));
    }

    public UserProfileResponse getUserProfile(Long userCode, String email) {
        Optional<User> userOptional = userMapper.findUserByUserCode(userCode);
        if (userOptional.isEmpty()) throw new BusinessException(ErrorCode.NOT_FOUND_USER);
        User user = userOptional.get();
        if (!Objects.equals(user.getEmail(), email)) throw new BusinessException(ErrorCode.INVALID_PARAMETER);
        return UserProfileResponse.builder()
                .userCode(user.getUserCode())
                .email(user.getEmail())
                .userLevel(user.getUserLevel())
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .build();
    }

    private void checkPassword(User user, String password) {
        if (!user.getPassword().equals(passwordEncoder.encrypt(password))) {
            throw new BusinessException(ErrorCode.MISMATCH_USER);
        }
    }

    private byte[] createDefaultImage() {
        byte[] imageInByte;

        try {
            File file = ResourceUtils.getFile(defaultImage);
            BufferedImage originalImage = ImageIO.read(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(originalImage, "jpg", baos);
            baos.flush();

            imageInByte = baos.toByteArray();
            return imageInByte;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

}
