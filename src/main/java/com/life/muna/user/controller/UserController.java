package com.life.muna.user.controller;

import com.life.muna.auth.dto.RefreshToken;
import com.life.muna.auth.dto.UnLock;
import com.life.muna.auth.repository.RefreshTokenRepository;
import com.life.muna.auth.util.JwtTokenProvider;
import com.life.muna.common.dto.CommonResponse;
import com.life.muna.location.dto.update.LocationUpdateRequest;
import com.life.muna.location.service.LocationService;
import com.life.muna.user.dto.info.UserLevelResponse;
import com.life.muna.user.dto.notice.FcmTokenRequest;
import com.life.muna.user.dto.signIn.SignInRequest;
import com.life.muna.user.dto.signOut.SignOutRequest;
import com.life.muna.user.dto.signUp.SignUpRequest;
import com.life.muna.user.dto.reissue.ReissueRequest;
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
import org.springframework.web.multipart.MultipartFile;

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
    private final LocationService locationService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider tokenProvider;

    /**
     * 이메일 중복 체크 API
     * */
    @ApiOperation(value = "이메일 중복 체크")
    @ApiResponse(
            responseCode = "200",
            description = "Successful operation",
            content = @Content(
                    schema = @Schema(implementation = CommonResponse.class),
                    examples = @ExampleObject(
                            name = "example",
                            value = """
                                    {
                                      "statusCode": 200,
                                      "data": {
                                        "result": true
                                      },
                                      "message": "이메일 중복이 아닙니다."
                                    }""")))
    @GetMapping("/duplicate/email")
    public ResponseEntity<CommonResponse> isDuplicatedEmail(@RequestParam String email) {
        LOG.info("isDuplicatedEmail email: " + email);
        Map<String, Boolean> data = new HashMap<String, Boolean>();
        boolean result = userService.isDuplicated("email", email);
        data.put("result", result);
        return ResponseEntity.ok()
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .data(data)
                        .message(result ? "이메일 중복입니다." : "이메일 중복이 아닙니다.") .build());
    }

    /**
     * 닉네임 중복 체크 API
     * */
    @ApiOperation(value = "닉네임 중복 체크")
    @ApiResponse(
            responseCode = "200",
            description = "Successful operation",
            content = @Content(
                    schema = @Schema(implementation = CommonResponse.class),
                    examples = @ExampleObject(
                            name = "example",
                            value = """
                                    {
                                      "statusCode": 200,
                                      "data": {
                                        "result": true
                                      },
                                      "message": "닉네임 중복이 아닙니다."
                                    }""")))
    @GetMapping("/duplicate/nickname")
    public ResponseEntity<CommonResponse> isDuplicatedNickname(@RequestParam String nickname) {
        LOG.info("isDuplicatedNickname nickname: " + nickname);
        Map<String, Boolean> data = new HashMap<String, Boolean>();
        boolean result = userService.isDuplicated("nickname", nickname);
        data.put("result", result);
        return ResponseEntity.ok()
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .data(data)
                        .message(result ? "닉네임 중복입니다." : "닉네임 중복이 아닙니다.") .build());
    }

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
                                      "message": "회원 가입 성공"
                                    }""")))
    public ResponseEntity<CommonResponse> signUp(@RequestPart(value="data") @Valid SignUpRequest signUpRequest,
                                                 @RequestPart(value="thumbnail", required = false) MultipartFile thumbnail, HttpServletRequest request) {
        LOG.info("uri : {}, data: {}", request.getRequestURI(), signUpRequest.toString());
        Map<String, Object> data = new HashMap<String, Object>();
        boolean result = userService.signUp(signUpRequest, thumbnail);
        data.put("result", result);
        return ResponseEntity.ok()
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .data(data)
                        .message("회원 가입 " + (result ? "성공" : "실패")).build());
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
                                        "accessToken" : "",
                                        "refreshToken" : ""
                                      },
                                      "message": "로그인 성공"
                                    }""")))
    public ResponseEntity<CommonResponse> signIn(@RequestBody @Valid SignInRequest signInRequest) {
        LOG.info("signIn email: " + signInRequest.getEmail());
        return ResponseEntity.ok()
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .data(userService.signIn(signInRequest))
                        .message("로그인 성공").build());
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
                                       "message": "로그아웃 성공"
                                    }""")))
    public ResponseEntity<CommonResponse> signOut(@RequestBody SignOutRequest signOutRequest) {
        LOG.info("signOut email: " + signOutRequest.getEmail());
        Map<String, Boolean> data = new HashMap<String, Boolean>();
        userService.signOut(signOutRequest);
        boolean result = refreshTokenRepository.delete(signOutRequest.getEmail());
        data.put("result", result);
        return ResponseEntity.ok()
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .data(data)
                        .message("로그아웃 "+ (result ? "성공" : "실패")).build());
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
                                        "accessToken" : "",
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

    @ApiOperation(value = "회원 등급 및 거래 내역 조회")
    @PostMapping("/level")
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
                                      "statusCode": 200,
                                      "data": {
                                        "userLevel": "Lv01",
                                        "levelMaxCnt": 0,
                                        "historyCnt": 0,
                                      },
                                      "message": "회원 등급 및 거래 내역 조회 성공"
                                    }""")))
    public ResponseEntity<CommonResponse> getMyLevel(HttpServletRequest request) {
        LOG.info("uri : {}", request.getRequestURI());
        String email = (String) request.getAttribute("email");
        UserLevelResponse response = userService.getMyLevel(email);
        return ResponseEntity.ok()
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .data(response)
                        .message("회원 등급 및 거래 내역 조회 성공").build());
    }

    /**
     * 유저 정보 조회 API
     * */
    @ApiOperation(value = "유저 정보 조회")
    @GetMapping("/me")
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
                                            "email" : "",
                                            "nickname" : ""
                                            "profileImage" : "",
                                          },
                                          "message": "유저 정보 조회 성공"
                                        }""")))
    public ResponseEntity<CommonResponse> getUserProfile(HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        return ResponseEntity.ok()
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .data(userService.getUserProfile(email))
                        .message("유저 정보 조회 성공").build());
    }

    /**
     * 사용자 위치 업데이트 API
     * */
    @ApiOperation(value= "사용자 위치 업데이트")
    @PutMapping("/location")
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
                                      "statusCode": 200,
                                      "data": {
                                        "locationDongCd": 1138063100,
                                        "location": "서울특별시 은평구 신사제1동"
                                      },
                                      "message": "사용자 위치 업데이트 성공"
                                    }""")))
    public ResponseEntity<CommonResponse> updateLocation(@RequestBody @Valid LocationUpdateRequest locationUpdateRequest, HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        return ResponseEntity.ok()
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .data(locationService.updateLocation(email, locationUpdateRequest))
                        .message("사용자 위치 업데이트 성공").build());
    }

    /**
     * 사용자 Fcm 저장 API
     * */
    @ApiOperation(value = "fcm 토큰 저장")
    @PostMapping("/fcm")
    @ApiResponse(
            responseCode = "200",
            description = "Successful operation",
            content = @Content(
                    schema = @Schema(implementation = CommonResponse.class),
                    examples = @ExampleObject(
                            name = "example",
                            value = """
                                    {
                                      "statusCode": 200,
                                      "data": {
                                        "result": true
                                      },
                                      "message": "fcm 토큰 저장 성공"
                                    }""")))
    public ResponseEntity<CommonResponse> updateFcmToken(@RequestBody @Valid FcmTokenRequest fcmTokenRequest, HttpServletRequest request) {
        LOG.info("uri : {}, data: {}", request.getRequestURI(), fcmTokenRequest.toString());
        String email = (String) request.getAttribute("email");
        Map<String, Boolean> data = new HashMap<String, Boolean>();
        boolean result = userService.updateFcmToken(email, fcmTokenRequest);
        data.put("result", result);

        return ResponseEntity.ok()
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .data(data)
                        .message("fcm 토큰 저장 성공") .build());
    }

}
