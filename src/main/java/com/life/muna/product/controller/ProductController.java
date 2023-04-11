package com.life.muna.product.controller;

import com.life.muna.common.dto.CommonResponse;
import com.life.muna.product.dto.ProductListRequest;
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

import javax.validation.Valid;

@Api(tags = "상품 API 정보 제공")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/muna/v1/products")
public class ProductController {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private final ProductService productService;

    @ApiOperation(value = "상품 목록 조회")
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
                                          "productCode": 1,
                                          "location": "서울특별시 은평구 증산동",
                                          "category": "C0002",
                                          "title": "타이틀1",
                                          "thumbnail": "",
                                          "productStatus": true,
                                          "mcoin": 1,
                                          "reqCnt": 0,
                                          "insertDate": "2023-04-07T01:44:33.000+00:00",
                                          "updateDate": "2023-04-07T01:44:33.000+00:00"
                                        }
                                      ],
                                      "message": "상품 목록 조회"
                                    }""")))
    public ResponseEntity<CommonResponse> getProductList(@RequestBody @Valid ProductListRequest productListRequest) {
        return ResponseEntity.ok()
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .data(productService.getProductList(productListRequest))
                        .message("상품 목록 조회").build());
    }

    @ApiOperation(value = "상품 상세 조회")
    @GetMapping("/{productCode}")
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
                                        "likes": 0,
                                        "views": 0,
                                        "userProfileImage": "",
                                        "userNickname": "마루도키",
                                        "userLevel": "BASIC",
                                        "insertDate": "2023-04-07T01:44:33.000+00:00",
                                        "updateDate": "2023-04-07T01:44:33.000+00:00"
                                      }
                                      "message": "상품 상세 조회"
                                    }""")))
    public ResponseEntity<CommonResponse> getProduct(@RequestParam Long userCode, @PathVariable Long productCode) {
        return ResponseEntity.ok()
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .data(productService.getProduct(userCode, productCode))
                        .message("상품 상세 조회").build());

    }

}


