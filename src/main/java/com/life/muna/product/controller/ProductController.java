package com.life.muna.product.controller;

import com.life.muna.common.dto.CommonResponse;
import com.life.muna.product.dto.ProductDetailRequest;
import com.life.muna.product.dto.ProductListRequest;
import com.life.muna.product.dto.ProductShareRequest;
import com.life.muna.product.service.ProductService;
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

@Api(tags = "상품 API 정보 제공")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/muna/v1/products")
public class ProductController {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private final ProductService productService;

    @ApiOperation(value = "상품 목록 조회")
    @PostMapping("")
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
                                          "location": "서울특별시 은평구 증산동",
                                          "category": "C002",
                                          "title": "타이틀1",
                                          "thumbnail": "byte[]",
                                          "productStatus": true,
                                          "mcoin": 1,
                                          "reqCnt": 0,
                                          "insertDate": "2023-04-07 10:44:33",
                                          "updateDate": "2023-04-10 14:06:24"
                                        }
                                      ],
                                      "message": "상품 목록 조회 성공"
                                    }""")))
    public ResponseEntity<CommonResponse> getProductList(@RequestBody @Valid ProductListRequest productListRequest, HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        return ResponseEntity.ok()
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .data(productService.getProductList(email, productListRequest))
                        .message("상품 목록 조회 성공").build());
    }

    @ApiOperation(value = "상품 상세 조회")
    @PostMapping("/detail")
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
                                        "productCode": 1,
                                        "content": "컨텐트1",
                                        "imageLink": "이미지링크1",
                                        "imageCnt": 3,
                                        "likes": 0,
                                        "views": 0,
                                        "location": "서울특별시 은평구 신사1동",
                                        "category": "C0002",
                                        "title: "타이틀1",
                                        "thumbnail": byte[],
                                        "productStatus": true,
                                        "mcoin": 1,
                                        "reqCnt": 0,
                                        "insertDate": "2023-04-07 10:44:33",
                                        "updateDate": "2023-04-07 10:44:33",
                                        "userNickname": "마루도키",
                                        "userProfileImage": byte[],
                                        "userLevel": "BASIC",
                                        "isRequested": false,
                                      }
                                      "message": "상품 상세 조회 성공"
                                    }""")))
    public ResponseEntity<CommonResponse> getProduct(@RequestBody @Valid ProductDetailRequest productDetailRequest) {
        return ResponseEntity.ok()
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .data(productService.getProduct(productDetailRequest))
                        .message("상품 상세 조회 성공").build());
    }

    @PostMapping("/detail/request")
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
                                      "message": "상품 나눔 요청"
                                    }""")))
    public ResponseEntity<CommonResponse> requestShareProduct(@RequestBody @Valid ProductShareRequest productShareRequest, HttpServletRequest request) {
        LOG.info("requestShareProduct productCode: " + productShareRequest.getProductCode());
        LOG.info("requestShareProduct productCode: " + productShareRequest.getProductCode());
        LOG.info("requestShareProduct userCode: " + productShareRequest.getUserCode());
        LOG.info("requestShareProduct requestContent: " + productShareRequest.getRequestContent());

        String email = (String) request.getAttribute("email");

        Map<String, Boolean> data = new HashMap<String, Boolean>();
        Boolean result = productService.requestShareProduct(email, productShareRequest);
        data.put("result", result);
        return ResponseEntity.ok()
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .data(data)
                        .message("상품 나눔 요청 " + (result ? "성공" : "실패")).build());
    }

}
