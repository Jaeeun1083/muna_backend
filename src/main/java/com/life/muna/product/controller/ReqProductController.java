package com.life.muna.product.controller;

import com.life.muna.common.dto.CommonResponse;
import com.life.muna.product.dto.ProductShareRequest;
import com.life.muna.product.service.ReqProductService;
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

@Api(tags = "상품 나눔 요청 API 정보 제공")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/muna/v1/products/request")
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
    @ApiOperation("상품 나눔 요청 API")
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
    public ResponseEntity<CommonResponse> requestShareProduct(@PathVariable Long productCode, @RequestBody @Valid ProductShareRequest productShareRequest, HttpServletRequest request) {
        LOG.info("requestShareProduct productCode: " + productCode);
        LOG.info("requestShareProduct userCode: " + productShareRequest.getUserCode());
        LOG.info("requestShareProduct requestContent: " + productShareRequest.getRequestContent());

        String email = (String) request.getAttribute("email");
        productShareRequest.setProductCode(productCode);

        Map<String, Boolean> data = new HashMap<String, Boolean>();
        Boolean result = reqProductService.requestShareProduct(email, productShareRequest);
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
    @ApiOperation(value = "상품 나눔 요청 내역 조회")
    @GetMapping("")
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
                                          "title": "타이틀1",
                                          "thumbnail": byte[],
                                          "requestCount": 0,
                                          "mcoin": 1,
                                          "productStatus": true,
                                        },
                                      ]
                                      "message": "상품 나눔 요청 내역 조회 성공"
                                    }""")))
    public ResponseEntity<CommonResponse> getProductReceived(@RequestParam Integer startProductCode, @RequestParam Integer productDataCnt, HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        return ResponseEntity.ok()
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .data(reqProductService.getRequestProductList(email, startProductCode, productDataCnt))
                        .message("상품 나눔 요청 내역 조회 성공").build());
    }

}
