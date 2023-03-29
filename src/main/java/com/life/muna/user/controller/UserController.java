package com.life.muna.user.controller;

import com.life.muna.auth.dto.RefreshToken;
import com.life.muna.auth.dto.UnLock;
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
import io.swagger.annotations.ApiParam;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Api(tags = {"유저 API 정보 제공"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/muna/v1/users")
public class UserController {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private final UserService userService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider tokenProvider;

    /**
     * 회원가입 API
     * */
    @UnLock
    @ApiOperation(value = "회원 가입")
    @PostMapping("/signUp")
    @ApiParam()
    @ApiResponse(
            responseCode = "200",
            description = "Successful operation",
            content = @Content(
                    schema = @Schema(implementation = CommonResponse.class),
                    examples = @ExampleObject(
                            name = "example",
                            value = """
                                    {
                                      "statusCode": "200",
                                      "data": {
                                        "result": true
                                      },
                                      "message": "회원 가입 결과"
                                    }""")))
    public ResponseEntity<CommonResponse> signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
        LOG.info("signUp email: " + signUpRequest.getEmail());
        LOG.info("signUp phone: " + signUpRequest.getPhone());
        LOG.info("signUp nickname: " + signUpRequest.getNickname());
        Map<String, Boolean> data = new HashMap<String, Boolean>();
        Boolean result = userService.signUp(signUpRequest);
        data.put("userCode", result);
        return ResponseEntity.ok()
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .data(result)
                        .message(signUpRequest.getEmail() + "의 회원 가입 결과").build());
    }

    /**
     * 로그인 API
     * */
    @UnLock
    @ApiOperation(value = "로그인")
    @PostMapping("/signIn")
    @ApiResponse(
            responseCode = "200",
            description = "Successful operation",
            content = @Content(
                    schema = @Schema(implementation = CommonResponse.class),
                    examples = @ExampleObject(
                            name = "example",
                            value = """
                                    {
                                      "statusCode": "200",
                                      "data": {
                                        "accessToken" : "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkZXZwc2dAa2FrYW8uY29tIiwiaWF0IjoxNjc5NzEyNzcxLCJleHAiOjE2Nzk3MTYzNzF9.aVVcjd9hlWB_O9aVwsHB70BTqpkuzgxmc15t7Y5KNr",
                                        "refreshToken" : "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkZXZwc2dAa2FrYW8uY29tIiwiaWF0IjoxNjc5NzEyNzcxLCJleHAiOjE2Nzk3NDg3NzF9.GpHapkeEpHDc8mu1SItrTXeaSRXBlr5ZZ0p8JyuVHIc"
                                      },
                                      "message": "로그인 결과"
                                    }""")))
    public ResponseEntity<CommonResponse> signIn(@RequestBody @Valid SignInRequest signInRequest) {
        LOG.info("signIn email: " + signInRequest.getEmail());
        return ResponseEntity.ok()
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .data(userService.signIn(signInRequest))
                        .message("로그인 결과").build());
    }

    /**
     * 로그아웃 API
     * */
    @UnLock
    @ApiOperation(value = "로그아웃")
    @PostMapping("/signOut")
    @ApiResponse(
            responseCode = "200",
            description = "Successful operation",
            content = @Content(
                    schema = @Schema(implementation = CommonResponse.class),
                    examples = @ExampleObject(
                            name = "example",
                            value = """
                                    {
                                      "statusCode": "200",
                                      "data": {
                                        "result": true
                                      },
                                      "message": "로그아웃 결과"
                                    }""")))
    public ResponseEntity<CommonResponse> signOut(@RequestBody SignOutRequest signOutRequest) {
        LOG.info("signOut email: " + signOutRequest.getEmail());
        Map<String, Boolean> data = new HashMap<String, Boolean>();
        Boolean result = refreshTokenRepository.delete(signOutRequest.getEmail());
        data.put("result", result);
        return ResponseEntity.ok()
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .data(data)
                        .message("로그아웃 결과").build());
    }

    /**
     * Access Token 재발급 API
     * */
    @UnLock
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
                                        "accessToken" : "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkZXZwc2dAa2FrYW8uY29tIiwiaWF0IjoxNjc5NzEyODU1LCJleHAiOjE2Nzk3MTY0NTV9.upxWRIj4vznnlOICVd2mAC2-bpHbX_BXFQCqiImx9HA",
                                      },
                                      "message": "access token 재발급 결과"
                                    }""")))
    public ResponseEntity<CommonResponse> reissueAccessToken(@RequestBody ReissueRequest reissueRequest) {
        LOG.info("reissueAccessToken email: " + reissueRequest.getEmail());
        LOG.info("reissueAccessToken refreshToken: " + reissueRequest.getRefreshToken());
        return ResponseEntity.ok()
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .data(tokenProvider.createAccessToken(reissueRequest.getEmail(), new RefreshToken(reissueRequest.getRefreshToken())))
                        .message("access token 재발급 결과").build());
    }

    @ApiOperation(value = "유저 정보 조회")
    @GetMapping("/{userCode}")
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
                                            "email" : "muna@munaApp.com",
                                            "nickname" : "마루도키"
                                            "profileImage" : "IMG_1673_(2)",
                                          },
                                          "message": "유저 정보 조회 결과"
                                        }""")))
    public ResponseEntity<CommonResponse> getUserProfile(@PathVariable int userCode,  HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        LOG.info("getUserProfile email: " + email);
        return ResponseEntity.ok()
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .data(userService.getUserProfile(userCode, email))
                        .message("유저 정보 조회 결과").build());
    }

}
