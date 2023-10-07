package com.life.muna.productReq.controller;

import com.life.muna.common.dto.CommonResponse;
import com.life.muna.product.dto.request.ProductShareRequest;
import com.life.muna.productReq.service.ReqProductService;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "상품 나눔 요청 API 정보 제공")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/muna/v1/products/requests")
public class ReqProductController {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private final ReqProductService reqProductService;

    /**
     * 보유한 나눔 신청 횟수 조회 API
     * */
    @ApiOperation(value = "보유한 나눔 신청 횟수 조회")
    @GetMapping("/possible")
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
                                        "result": 0
                                      }
                                      "message": "보유한 나눔 신청 횟수 조회 성공"
                                    }""")))
    public ResponseEntity<CommonResponse> getMyRequestCount(HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        Map<String, Integer> data = new HashMap<String, Integer>();
        Integer requested = reqProductService.getMyRequestCount(email);
        data.put("result", requested);
        return ResponseEntity.ok()
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .data(data)
                        .message("보유한 나눔 신청 횟수 조회 성공").build());
    }

    /**
     * 상품 나눔 요청 API
     * */
    @ApiOperation(value = "나눔 요청")
    @PostMapping("/{productCode}")
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
                                        "result": true
                                      }
                                      "message": "상품 나눔 요청 성공"
                                    }""")))
    public ResponseEntity<CommonResponse> requestShareProduct(@RequestBody @Valid ProductShareRequest productShareRequest, HttpServletRequest request) {
        LOG.info("uri : {}, data: {}", request.getRequestURI(), productShareRequest.toString());

        String email = (String) request.getAttribute("email");


        Map<String, Boolean> data = new HashMap<String, Boolean>();
        boolean result = reqProductService.requestShareProduct(email, productShareRequest);
        data.put("result", result);
        return ResponseEntity.ok()
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .data(data)
                        .message("상품 나눔 요청 " + (result ? "성공" : "실패")).build());
    }

    /**
     * 상품 나눔 요청 취소 API
     * */
    @ApiOperation(value = "상품 나눔 요청 취소")
    @DeleteMapping("/{productCode}")
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
                                        "result": true
                                      }
                                      "message": "상품 나눔 요청 취소 성공"
                                    }""")))
    public ResponseEntity<CommonResponse> withdrawRequestProduct(@PathVariable Long productCode, HttpServletRequest request) {
        String email = (String) request.getAttribute("email");

        Map<String, Boolean> data = new HashMap<String, Boolean>();
        boolean result = reqProductService.withdrawRequestProduct(email, productCode);
        data.put("result", result);
        return ResponseEntity.ok()
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .data(data)
                        .message("상품 나눔 요청 취소 " + (result ? "성공" : "실패")).build());
    }

    /**
     * 상품 나눔 요청 내역 조회 API
     * */
    @Transactional
    @ApiOperation(value = "받은 요청 리스트 조회")
    @GetMapping("/requested/list")
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
                                      "data": [
                                        {
                                          "productCode": 1,
                                          "title": "타이틀1",
                                          "thumbnail": byte[],
                                          "reqCnt": 0,
                                          "mcoin": 1,
                                          "productStatus": true,
                                          },
                                        ]
                                      "message": "상품 나눔 요청 내역 조회 성공"
                                    }""")))
    public ResponseEntity<CommonResponse> getRequestedProduct(HttpServletRequest request) {
        String email = (String) request.getAttribute("email");

        return ResponseEntity.ok()
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .data(reqProductService.getRequestProductList(email))
                        .message("상품 나눔 요청 내역 조회 성공").build());
    }

    @ApiOperation(value = "요청 리스트 조회")
    @GetMapping("/received/list")
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
                                      "data": [{
                                            "productCode": 1,
                                            "title": "타이틀1",
                                            "thumbnail": byte[],
                                            "reqCnt": 1,
                                            "mcoin": 1,
                                            "productStatus": "AVL",
                                            "isRead": false,
                                        }],
                                      "message": "상품 받은 신청 리스트 조회 성공"
                                    }""")))
    public ResponseEntity<CommonResponse> getReceivedProductList(HttpServletRequest request) {
        LOG.info("uri : {}", request.getRequestURI());
        String email = (String) request.getAttribute("email");

        return ResponseEntity.ok()
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .data(reqProductService.getReceivedProductList(email))
                        .message("상품 받은 신청 리스트 조회 성공").build());
    }

    @ApiOperation(value = "요청자 리스트 조회")
    @PostMapping("/received/detail")
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
                                        "product": {
                                          "productCode": 1,
                                          "title": "타이틀1",
                                          "thumbnail": "섬네일1",
                                          "reqCnt": 0,
                                          "mcoin": 1,
                                          "productStatus": "AVL",
                                        },
                                        "reqUserProfile": [
                                          {
                                            "productReqCode": 1,
                                            "roomCode": "",
                                            "userNickname": "마루도키",
                                            "userLevel": "",
                                            "userProfileImage": byte[],
                                            "location": "서울특별시 은평구 신사1동",
                                            "reqContent": "나눔 요청1",
                                            "reqRead": true,
                                            "reqStatus": "REQ",
                                            "insertDate": "2023-04-07 10:44:33",
                                            "updateDate": "2023-04-07 10:44:33",
                                          },
                                        ],
                                      }
                                      "message": "상품에 대한 요청 조회 성공"
                                    }""")))
    public ResponseEntity<CommonResponse> getReceivedReqOfProduct(@RequestBody @NotNull Long productCode, HttpServletRequest request) {
        LOG.info("uri : {}, data: {}", request.getRequestURI(), productCode.toString());
        String email = (String) request.getAttribute("email");
        return ResponseEntity.ok()
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .data(reqProductService.getReceivedReqOfProduct(email, productCode))
                        .message("상품에 대한 요청 조회 성공").build());
    }

}
