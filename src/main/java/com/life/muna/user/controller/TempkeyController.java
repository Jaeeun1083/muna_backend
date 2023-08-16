package com.life.muna.user.controller;

import com.life.muna.common.dto.CommonResponse;
import com.life.muna.user.dto.signUp.IssueTempKeyRequest;
import com.life.muna.user.dto.signUp.IssueTempKeyResponse;
import com.life.muna.user.dto.signUp.VerifyTempKeyRequest;
import com.life.muna.user.service.TempkeyService;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Api(tags = {"temp key 발급 API 정보 제공"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/muna/v1/tempkey")
public class TempkeyController {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private final TempkeyService tempkeyService;


    /**
     * 본인 인증 코드 발급 API
     * */
    @ApiOperation(value = "본인 인증 코드 발급")
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
                                        "result": true,
                                        "phone": "",
                                        "possibleCnt": 0,
                                        "leftTime": 0
                                      },
                                      "message": "본인 인증 코드 발급 성공"
                                    }""")))
    @PostMapping("/issue")
    public ResponseEntity<CommonResponse> issueTempKey(@RequestBody @Valid IssueTempKeyRequest issueTempKeyRequest, HttpServletRequest request) {
        LOG.info("uri : {}, data: {}", request.getRequestURI(), issueTempKeyRequest.toString());
        IssueTempKeyResponse response = tempkeyService.issueTempKey(issueTempKeyRequest);
        return ResponseEntity.ok()
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .data(response)
                        .message("본인 인증 코드 발급 "  + (response.getResult() ? "성공" : "실패")) .build());
    }

    /**
     * 본인 인증 코드 유효성 체크 API
     * */
    @ApiOperation(value = "본인 인증 코드 유효성 체크")
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
                                        "phone": "",
                                        "result": true
                                      },
                                      "message": "본인 인증 코드 유효성 체크 성공"
                                    }""")))
    @PostMapping("/verify")
    public ResponseEntity<CommonResponse> verifyTempKey(@RequestBody @Valid VerifyTempKeyRequest verifyTempKeyRequest, HttpServletRequest request) {
        LOG.info("uri : {}, data: {}", request.getRequestURI(), verifyTempKeyRequest.toString());
        Map<String, Object> data = new HashMap<String, Object>();
        boolean result = tempkeyService.verifyTempKey(verifyTempKeyRequest);
        data.put("result", result);
        data.put("phone", verifyTempKeyRequest.getPhone());
        return ResponseEntity.ok()
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .data(data)
                        .message("본인 인증 코드 유효성 체크 " + (result ? "성공" : "실패")) .build());
    }

}
