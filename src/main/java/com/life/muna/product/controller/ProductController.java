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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Api(tags = "상품 API 정보 제공")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/muna/v1/products")
public class ProductController {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private final ProductService productService;

    /**
     * 상품 목록 조회 API
     * */
    @ApiOperation(value = "상품 목록 조회")
    @PostMapping("/list")
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

    /**
     * 상품 상세 조회 API
     * */
    @ApiOperation(value = "상품 상세 조회")
    @GetMapping("/detail")
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
                                        "isLiked": false
                                      }
                                      "message": "상품 상세 조회 성공"
                                    }""")))
    public ResponseEntity<CommonResponse> getProduct(@RequestParam Long productCode, HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        return ResponseEntity.ok()
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .data(productService.getProduct(email, productCode))
                        .message("상품 상세 조회 성공").build());
    }

    /**
     * 상품 등록 내역 조회 API
     * */
    @ApiOperation(value = "상품 등록 내역 조회")
    @GetMapping("/register/list")
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
                                          "requestCount": 0,
                                          "mcoin": 1,
                                          "productStatus": true,
                                        },
                                      ]
                                      "message": "상품 등록 내역 조회 성공"
                                    }""")))
    public ResponseEntity<CommonResponse> getRegisteredProductList(@RequestParam int page, HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        return ResponseEntity.ok()
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .data(productService.getRegisteredProductList(email, page))
                        .message("상품 등록 내역 조회 성공").build());
    }

}
