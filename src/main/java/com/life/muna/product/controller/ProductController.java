package com.life.muna.product.controller;

import com.life.muna.auth.util.JwtTokenProvider;
import com.life.muna.common.dto.CommonResponse;
import com.life.muna.product.dto.ProductListRequest;
import com.life.muna.product.dto.ProductListResponse;
import com.life.muna.product.dto.ProductRegiListResponse;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "상품 API 정보 제공")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/muna/v1/products")
public class ProductController {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private final ProductService productService;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 상품 목록 조회 API
     * */
    @Transactional
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
                                      "data": {
                                        "info": {
                                          "productCount": 4,
                                          "productCode": 45
                                        },
                                        "result": [
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
                                      },
                                      "message": "상품 목록 조회 성공"
                                    }""")))
    public ResponseEntity<CommonResponse> getProductList(@RequestBody @Valid ProductListRequest productListRequest, HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        Map<String, Object> data = new HashMap<>();
        if (productListRequest.getMaxProductCode() == null || productListRequest.getMaxProductCode() == 0) {
            data.put("info", productService.getMaxProductInfo());
        } else {
            data.put("info", null);
        }

        List<ProductListResponse> result = productService.getProductList(email, productListRequest);
        data.put("result", result);

        return ResponseEntity.ok()
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .data(data)
                        .message("상품 목록 조회 성공").build());
    }

    /**
     * 상품 상세 조회 API
     * */
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
    public ResponseEntity<CommonResponse> getProduct(@PathVariable Long productCode, HttpServletRequest request) {
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
    @Transactional
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
                                      "data": {
                                        "info": {
                                          "productCount": 4,
                                          "productCode": 45
                                        },
                                        "result": [
                                          {
                                            "productCode": 28,
                                            "title": "string",
                                            "thumbnail": "",
                                            "reqCnt": 0,
                                            "mcoin": 0,
                                            "productStatus": false
                                          },
                                        ],
                                      },
                                      "message": "상품 등록 내역 조회 성공"
                                    }""")))
    public ResponseEntity<CommonResponse> getRegisteredProductList(@RequestParam int page, Long maxProductCode, HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        Long userCode = jwtTokenProvider.getUserCodeFromEmail(email);

        Map<String, Object> data = new HashMap<>();
        if (maxProductCode == null || maxProductCode == 0) {
            data.put("info", productService.getMaxRegisteredProductInfo(userCode));
        } else {
            data.put("info", null);
        }

        List<ProductRegiListResponse> result = productService.getRegisteredProductList(userCode, page);
        data.put("result", result);
        return ResponseEntity.ok()
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .data(data)
                        .message("상품 등록 내역 조회 성공").build());
    }

}
