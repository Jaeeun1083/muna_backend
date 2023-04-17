package com.life.muna.product.service;

import com.life.muna.common.error.ErrorCode;
import com.life.muna.common.error.exception.BusinessException;
import com.life.muna.location.domain.Location;
import com.life.muna.location.mapper.LocationMapper;
import com.life.muna.product.domain.Product;
import com.life.muna.product.domain.enums.LocationRange;
import com.life.muna.product.dto.*;
import com.life.muna.product.mapper.ProductMapper;
import com.life.muna.product.mapper.ReqProductMapper;
import com.life.muna.user.domain.User;
import com.life.muna.user.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final Logger LOG = LoggerFactory.getLogger(ProductService.class);

    private ProductMapper productMapper;
    private UserMapper userMapper;
    private ReqProductMapper reqProductMapper;
    private LocationMapper locationMapper;

    public ProductService(ProductMapper productMapper, UserMapper userMapper, ReqProductMapper reqProductMapper, LocationMapper locationMapper) {
        this.productMapper = productMapper;
        this.userMapper = userMapper;
        this.reqProductMapper = reqProductMapper;
        this.locationMapper = locationMapper;
    }

    public List<ProductListResponse> getProductList(String emailFromToken, ProductListRequest productListRequest) {
        validateEmailFromTokenAndUserCode(emailFromToken, productListRequest.getUserCode());
        User user = userMapper.findUserByUserCode(productListRequest.getUserCode()).orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_BY_USER_CODE));
        if(user.getLocationDongCd() == null) throw new BusinessException(ErrorCode.UNPROCESSABLE_USER_LOCATION);
        Location location = locationMapper.findByLocationDongCd(user.getLocationDongCd());
        List<ProductListResponse> productList;
        LocationRange locationRange = LocationRange.fromCode(productListRequest.getLocationRange());

        Long locationCode = switch (locationRange) {
            case L001 -> subStringValue(0, 2, location.getLocationDongCd());
            case L002 -> subStringValue(0, 5, location.getLocationDongCd());
            case L003 -> location.getLocationDongCd();
            default -> null;
        };
        productListRequest.setLocationCode(locationCode);
        productList = productMapper.findProductList(productListRequest);
        return productList;
    }

    public ProductDetailResponse getProduct(ProductDetailRequest productDetailRequest) {
        Long userCode = productDetailRequest.getUserCode();
        Long productCode = productDetailRequest.getProductCode();

        Optional<ProductDetailResponse> productDetailOptional = productMapper.findProductDetailByProductCode(productCode);
        if (productDetailOptional.isEmpty()) throw new BusinessException(ErrorCode.NOT_FOUND_PRODUCT_DETAIL);
        ProductDetailResponse productDetailResponse = productDetailOptional.get();
        Long sellerCode = productDetailResponse.getUserCode();
        Optional<User> sellerOptional = userMapper.findUserByUserCode(sellerCode);
        if (sellerOptional.isEmpty()) throw new BusinessException(ErrorCode.NOT_FOUND_SELLER);
        productDetailResponse.setSellerData(sellerOptional.get());

        //TODO 회원당 조회수 중복 X -> 조회수 테이블 필요. 무조건 조회수 늘어남 -> product detail 테이블의 like +1
        boolean isRequested = reqProductMapper.existsByUserCodeAndProductCode(userCode, productCode);
        return productDetailResponse.setRequested(isRequested);
    }

    private Long subStringValue(int beginIndex, int endIndex, Long value) {
        String strValue = Long.toString(value);
        return Long.parseLong(strValue.substring(beginIndex, endIndex));
    }

    private void validateEmailFromTokenAndUserCode(String emailFromToken, Long userCode) {
        Long findUserCode = userMapper.findUserCodeByEmail(emailFromToken);
        if (findUserCode == null) throw new BusinessException(ErrorCode.NOT_FOUND_BY_USER_CODE);
        if (!findUserCode.equals(userCode)) throw new BusinessException(ErrorCode.MISMATCH_TOKEN_USER_CODE);
    }

}
