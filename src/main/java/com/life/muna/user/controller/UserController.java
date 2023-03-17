package com.life.muna.user.controller;

import com.life.muna.common.dto.CommonResponse;
import com.life.muna.user.dto.SignInRequest;
import com.life.muna.user.dto.SignUpRequest;
import com.life.muna.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api(tags = {"유저 API 정보 제공"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/muna/v1/users")
public class UserController {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private final UserService userService;

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

}
