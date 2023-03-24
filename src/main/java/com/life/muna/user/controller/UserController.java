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
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @ApiResponse(
            responseCode = "200",
            description = "Successful operation",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CommonResponse.class),
                    examples = @ExampleObject(
                            name = "example",
                            value = """
                                    {
                                      "statusCode": "200",
                                      "data": {
                                        "int"
                                      },
                                      "message": "회원 가입 결과"
                                    }""")))
    public ResponseEntity<CommonResponse> signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
        LOG.info("signUp email: {}" + signUpRequest.getEmail());
        LOG.info("signUp phone: {}" + signUpRequest.getPhone());
        LOG.info("signUp nickname: {}" + signUpRequest.getNickname());
        return ResponseEntity.ok()
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .data(userService.signUp(signUpRequest))
                        .message("회원 가입 결과").build());
    }

    @ApiOperation(value = "로그인")
    @PostMapping("/signIn")
    @ApiResponse(
            responseCode = "200",
            description = "Successful operation",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CommonResponse.class),
                    examples = @ExampleObject(
                            name = "example",
                            value = """
                                    {
                                      "statusCode": "200",
                                      "data": {
                                        "accessToken" : "string",
                                        "refreshToken" : "string"
                                      },
                                      "message": "로그인 결과"
                                    }""")))
    public ResponseEntity<CommonResponse> signIn(@RequestBody @Valid SignInRequest signInRequest) {
        LOG.info("signIn email: {}" + signInRequest.getEmail());
        return ResponseEntity.ok()
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .data(userService.signIn(signInRequest))
                        .message("로그인 결과").build());
    }

    @ApiOperation(value = "로그아웃")
    @PostMapping("/signOut")
    @ApiResponse(
            responseCode = "200",
            description = "Successful operation",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CommonResponse.class),
                    examples = @ExampleObject(
                            name = "example",
                            value = """
                                    {
                                      "statusCode": "200",
                                      "data": {
                                        "Boolean"
                                      },
                                      "message": "로그아웃 결과"
                                    }""")))
    public ResponseEntity<CommonResponse> signOut(@RequestBody SignOutRequest signOutRequest) {
        LOG.info("signOut email: {}" + signOutRequest.getEmail());
        return ResponseEntity.ok()
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .data(refreshTokenRepository.delete(signOutRequest.getEmail()))
                        .message("로그아웃 결과").build());
    }

    @ApiOperation(value = "Access Token 재발급")
    @PostMapping("/reissue")
    @ApiResponse(
            responseCode = "200",
            description = "Successful operation",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CommonResponse.class),
                    examples = @ExampleObject(
                            name = "example",
                            value = """
                                    {
                                      "statusCode": "200",
                                      "data": {
                                        "accessToken" : "string",
                                      },
                                      "message": "access token 재발급 결과"
                                    }""")))
    public ResponseEntity<CommonResponse> reissueAccessToken(@RequestBody ReissueRequest reissueRequest) {
        LOG.info("reissueAccessToken email: {}" + reissueRequest.getEmail());
        LOG.info("reissueAccessToken refreshToken: {}" + reissueRequest.getRefreshToken().getRefreshToken());
        return ResponseEntity.ok()
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .data(tokenProvider.createAccessToken(reissueRequest.getEmail(), reissueRequest.getRefreshToken()))
                        .message("access token 재발급 결과").build());
    }

}
