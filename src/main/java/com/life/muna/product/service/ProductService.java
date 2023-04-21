package com.life.muna.product.service;

import com.life.muna.auth.util.JwtTokenProvider;
import com.life.muna.common.error.ErrorCode;
import com.life.muna.common.error.exception.BusinessException;
import com.life.muna.like.mapper.ProductLikeMapper;
import com.life.muna.location.domain.Location;
import com.life.muna.location.mapper.LocationMapper;
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
    private final static Integer PAGE_SIZE = 30;

    private ProductMapper productMapper;
    private ReqProductMapper reqProductMapper;
    private ProductLikeMapper productLikeMapper;
    private UserMapper userMapper;
    private LocationMapper locationMapper;
    private JwtTokenProvider jwtTokenProvider;

    public ProductService(ProductMapper productMapper, ReqProductMapper reqProductMapper, ProductLikeMapper productLikeMapper, UserMapper userMapper, LocationMapper locationMapper, JwtTokenProvider jwtTokenProvider) {
        this.productMapper = productMapper;
        this.reqProductMapper = reqProductMapper;
        this.productLikeMapper = productLikeMapper;
        this.userMapper = userMapper;
        this.locationMapper = locationMapper;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public List<ProductListResponse> getProductList(String emailFromToken, ProductListRequest productListRequest) {
//        jwtTokenProvider.validateEmailFromTokenAndUserCode(emailFromToken, productListRequest.getUserCode());
//        User user = userMapper.findUserByUserCode(productListRequest.getUserCode()).orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_BY_USER_CODE));
        User user =  userMapper.findUserByEmail(emailFromToken).orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_BY_USER_CODE));
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
        productListRequest.setPageInfo(PAGE_SIZE);
        productList = productMapper.findProductList(productListRequest);
        return productList;
    }

    public ProductDetailResponse getProduct(String emailFromToken, Long productCode) {
        Long userCode = userMapper.findUserCodeByEmail(emailFromToken);

        Optional<ProductDetailResponse> productDetailOptional = productMapper.findProductDetailByProductCode(productCode);
        if (productDetailOptional.isEmpty()) throw new BusinessException(ErrorCode.NOT_FOUND_PRODUCT_DETAIL);
        ProductDetailResponse productDetailResponse = productDetailOptional.get();
        Long sellerCode = productDetailResponse.getUserCode();
        Optional<User> sellerOptional = userMapper.findUserByUserCode(sellerCode);
        if (sellerOptional.isEmpty()) throw new BusinessException(ErrorCode.NOT_FOUND_SELLER);
        productDetailResponse.setSellerData(sellerOptional.get());

        //TODO 회원당 조회수 중복 X -> 조회수 테이블 필요. 무조건 조회수 늘어남 -> product detail 테이블의 like +1
        boolean isRequested = reqProductMapper.existsByUserCodeAndProductCode(userCode, productCode);
        boolean isLiked = productLikeMapper.existsByUserCodeAndProductCode(userCode, productCode);
        return productDetailResponse.setMyInformation(isRequested, isLiked);
    }

    private Long subStringValue(int beginIndex, int endIndex, Long value) {
        String strValue = Long.toString(value);
        return Long.parseLong(strValue.substring(beginIndex, endIndex));
    }

}
