package com.life.muna.product.dto;

import com.life.muna.common.util.EnumValue;
import com.life.muna.product.domain.Product;
import com.life.muna.product.domain.ProductDetail;
import com.life.muna.product.domain.enums.Category;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

import static com.life.muna.product.domain.enums.Category.fromCode;

@Getter
public class ProductCreateRequest {

    @ApiModelProperty(example = "C001", required = true)
    @EnumValue(enumClass = Category.class, message = "카테고리 코드가 올바르지 않습니다")
    private String category;

    @NotBlank(message = "나눔 상품명은 필수 입력 값입니다.")
    @Size(max = 40)
    private String title;

    private byte[] thumbnail;

    private List<byte[]> images;

    @NotBlank(message = "나눔 상품설명은 필수 입력 값입니다.")
    private String content;

    @Min(value = 1, message = "무나 코인 가격은 1 이상이어야 합니다.")
    private Integer mcoin;

    public static Product toProduct(ProductCreateRequest productCreateRequest, Long userCode, Long locationDongCd, Date date) {
        return Product.builder()
                .userCode(userCode)
                .locationDongCd(locationDongCd)
                .category(fromCode(productCreateRequest.getCategory()))
                .title(productCreateRequest.getTitle())
                .thumbnail(productCreateRequest.getThumbnail())
                .productStatus(true)
                .mcoin(productCreateRequest.getMcoin())
                .insertDate(date)
                .updateDate(date)
                .build();
    }

    public static ProductDetail toProductDetail(ProductCreateRequest productCreateRequest, Long userCode, Long productCode, String imageLink, Integer imageCnt, Date date) {
        return ProductDetail.builder()
                .productCode(productCode)
                .userCode(userCode)
                .content(productCreateRequest.getContent())
                .imageLink(imageLink)
                .imageCnt(imageCnt)
                .insertDate(date)
                .updateDate(date)
                .build();
    }

}
