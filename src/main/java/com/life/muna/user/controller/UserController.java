package com.life.muna.user.controller;

import com.life.muna.auth.dto.RefreshToken;
import com.life.muna.auth.repository.RefreshTokenRepository;
import com.life.muna.auth.util.JwtTokenProvider;
import com.life.muna.common.dto.CommonResponse;
import com.life.muna.user.dto.ReissueRequest;
import com.life.muna.user.dto.SignInRequest;
import com.life.muna.user.dto.SignOutRequest;
import com.life.muna.user.dto.SignUpRequest;
import com.life.muna.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = {"유저 API 정보 제공"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/muna/v1/users")
public class UserController {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private final UserService userService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider tokenProvider;

    @ApiOperation(value = "회원 가입")
    @PostMapping("/signUp")
    public ResponseEntity<CommonResponse> signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
        return ResponseEntity.ok()
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .data(userService.signUp(signUpRequest))
                        .message("회원 가입 성공").build());
    }

    @ApiOperation(value = "로그인")
    @PostMapping("/signIn")
    public ResponseEntity<CommonResponse> signIn(@RequestBody @Valid SignInRequest signInRequest) {
        return ResponseEntity.ok()
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .data(userService.signIn(signInRequest))
                        .message("로그인 성공").build());
    }

    @ApiOperation(value = "로그아웃")
    @PostMapping("/signOut")
    public ResponseEntity<CommonResponse> signOut(@RequestBody SignOutRequest signOutRequest) {
        return ResponseEntity.ok()
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .data(refreshTokenRepository.delete(signOutRequest.getEmail()))
                        .message("로그아웃").build());
    }

    @ApiOperation(value = "Access Token 재발급")
    @PostMapping("/reissue")
    public ResponseEntity<CommonResponse> reissueAccessToken(@RequestBody ReissueRequest reissueRequest) {
        System.out.println("email: {}" + reissueRequest.getEmail());
        System.out.println("refreshToken: {}" + reissueRequest.getRefreshToken());
        return ResponseEntity.ok()
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .data(tokenProvider.createAccessToken(reissueRequest.getEmail(), reissueRequest.getRefreshToken()))
                        .message("access token 재발급").build());
    }

}
