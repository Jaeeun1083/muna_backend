package com.life.muna.like.controller;

import com.life.muna.common.dto.CommonResponse;
import com.life.muna.like.dto.ProductLikeListResponse;
import com.life.muna.like.service.ProductLikeService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "관심 내역 API 정보 제공")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/muna/v1/products/likes")
public class ProductLikeController {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private final ProductLikeService productLikeService;

    /**
     * 상품 좋아요 요청 API
     * */
    @ApiOperation(value = "상품 좋아요 요청")
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
                                      "message": "상품 좋아요 요청 성공"
                                    }""")))
    public ResponseEntity<CommonResponse> likeProduct(@PathVariable Long productCode, HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        LOG.info("likeProduct email: " + email);
        LOG.info("likeProduct productCode: " + productCode);

        Map<String, Boolean> data = new HashMap<String, Boolean>();
        boolean result = productLikeService.likeProduct(email, productCode);
        data.put("result", result);
        return ResponseEntity.ok()
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .data(data)
                        .message("상품 좋아요 요청 " + (result ? "성공" : "실패")).build());
    }

    /**
     * 상품 좋아요 요청 취소 API
     * */
    @ApiOperation(value = "상품 좋아요 요청 취소")
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
                                      "message": "상품 좋아요 요청 취소 성공"
                                    }""")))
    public ResponseEntity<CommonResponse> withdrawLikeProduct(@PathVariable Long productCode, HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        LOG.info("withdrawLikeProduct email: " + email);
        LOG.info("withdrawLikeProduct productCode: " + productCode);

        Map<String, Boolean> data = new HashMap<String, Boolean>();
        boolean result = productLikeService.withdrawLikeProduct(email, productCode);
        data.put("result", result);
        return ResponseEntity.ok()
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .data(data)
                        .message("상품 좋아요 요청 취소 " + (result ? "성공" : "실패")).build());
    }

    /**
     * 상품 좋아요 요청 내역 조회 API
     * */
    @Transactional
    @ApiOperation(value = "상품 좋아요 요청 내역 조회")
    @GetMapping("/list")
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
                                            "title": "타이틀1",
                                            "thumbnail": byte[],
                                            "reqCnt": 0,
                                            "mcoin": 1,
                                            "productStatus": true,
                                            "insertDate": "2023.04.07 10:44:33",
                                            "updateDate": "2023.04.20 12:57:59"
                                          },
                                        ]
                                      },
                                      "message": "상품 좋아요 요청 내역 조회 성공"
                                    }""")))
    public ResponseEntity<CommonResponse> getProductLiked(@RequestParam int page, Long maxProductCode, HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        Map<String, Object> data = new HashMap<>();
        if (maxProductCode == null || maxProductCode == 0) {
            data.put("info", productLikeService.getMaxProductLikeInfo());
        } else {
            data.put("info", null);
        }
        List<ProductLikeListResponse> result = productLikeService.getProductLiked(email, page, maxProductCode);
        data.put("result", result);
        return ResponseEntity.ok()
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .data(data)
                        .message("상품 좋아요 요청 내역 조회 성공").build());
    }

}
