package com.life.muna.product.controller;

import com.life.muna.auth.util.JwtTokenProvider;
import com.life.muna.chat.domain.ChatRoom;
import com.life.muna.chat.domain.enums.ChatStatus;
import com.life.muna.common.dto.CommonResponse;
import com.life.muna.product.dto.create.ProductCreateRequest;
import com.life.muna.product.dto.list.ProductListRequest;
import com.life.muna.product.dto.list.ProductListResponse;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.Duration;
import java.util.*;

@Api(tags = "상품 API 정보 제공")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/muna/v1/products")
public class ProductController {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private final ProductService productService;

    /**
     * 나눔 상품 등록 API
     * */
    @ApiOperation(value = "나눔 상품 등록")
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
                                      "data": {
                                        "productCode": 1,
                                      }
                                      "message": "나눔 상품 등록 성공"
                                    }""")))
    public ResponseEntity<CommonResponse> createProduct(@Valid @RequestPart(value="data") ProductCreateRequest productCreateRequest,
                                                        @RequestPart(value="thumbnail") MultipartFile thumbnail,
                                                        @RequestPart(value="images", required=false) List<MultipartFile> images,
                                                        HttpServletRequest request) {
        String email = (String) request.getAttribute("email");

        Map<String, Long> data = new HashMap<>();
        Long productCode = productService.createProduct(email, productCreateRequest, thumbnail, images);
        data.put("productCode", productCode);
        return ResponseEntity.ok()
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .data(data)
                        .message("나눔 상품 등록 성공").build());
    }

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
                                          "maxPage": 4,
                                          "maxCode": 45
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

        data.put("info", productService.getMaxProductInfo(productListRequest));

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
                                        "isMyProduct": false,
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
     * 상품에 대한 내 Like, Request 조회
     * */
    @ApiOperation(value = "상품에 대한 내 정보 조회")
    @GetMapping("/{productCode}/me")
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
                                        "isLiked": false,
                                        "isRequested": false,
                                      }
                                      "message": "상품에 대한 내 정보 조회 성공"
                                    }""")))
    public ResponseEntity<CommonResponse> getMyInfoByProduct(@PathVariable Long productCode, HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        return ResponseEntity.ok()
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .data(productService.getMyInfoOfProductResponse(email, productCode))
                        .message("상품에 대한 내 정보 조회 성공").build());
    }

    @ApiOperation(value = "나눔 삭제")
    @PostMapping("/withdraw")
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
                                      "message": "나눔 삭제 성공"
                                    }""")))
    public ResponseEntity<CommonResponse> withDrawProduct(@RequestBody Long productCode, HttpServletRequest request) {
        LOG.info("uri : {}, data: {}", request.getRequestURI(), "productCode : "  + productCode);
        String email = (String) request.getAttribute("email");
        Map<String, Boolean> data = new HashMap<>();
        boolean result = productService.withDrawProduct(email, productCode);
        data.put("result", true);
        return ResponseEntity.ok()
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .data(data)
                        .message("나눔 삭제 성공").build());
    }

    @ApiOperation(value = "상품 끌어올리기")
    @PostMapping("/lift-up")
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
                                      "message": "상품 끌어올리기 성공"
                                    }""")))
    public ResponseEntity<CommonResponse> liftUpProduct(@RequestBody Long productCode, HttpServletRequest request) {
        LOG.info("uri : {}, data: {}", request.getRequestURI(), "productCode : "  + productCode);
        String email = (String) request.getAttribute("email");
        Map<String, Boolean> data = new HashMap<>();
        long result = productService.liftUpProduct(email, productCode);
        data.put("result", result == 0L);

        Duration duration = Duration.ofSeconds(result);
        long days = duration.toDays();
        long hours = duration.toHours() % 24;

        return ResponseEntity.ok()
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .data(data)
                        .message(result == 0L ? "상품 끌어올리기 성공" : days + "일 " + hours + "시간 후 끌어올리기가 가능합니다.").build());
    }

}
