package com.life.muna.product.service;

import com.life.muna.common.error.ErrorCode;
import com.life.muna.common.error.exception.BusinessException;
import com.life.muna.product.domain.LocationRange;
import com.life.muna.product.domain.Product;
import com.life.muna.product.domain.ProductDetail;
import com.life.muna.product.dto.ProductDetailResponse;
import com.life.muna.product.dto.ProductListRequest;
import com.life.muna.product.mapper.ProductMapper;
import com.life.muna.user.domain.User;
import com.life.muna.user.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private ProductMapper productMapper;
    private UserMapper userMapper;
    private final Logger LOG = LoggerFactory.getLogger(ProductService.class);

    public ProductService(ProductMapper productMapper, UserMapper userMapper) {
        this.productMapper = productMapper;
        this.userMapper = userMapper;
    }

    public List<Product> getProductList(ProductListRequest productListRequest) {
        List<Product> productList;
        LocationRange locationRange = LocationRange.fromLocationRange(productListRequest.getLocationRange());
        Integer locationCode = productListRequest.getLocationCode();
        if ((locationRange != LocationRange.ALL) && (locationCode == null)) throw new BusinessException(ErrorCode.UNPROCESSABLE_PRODUCT_LOCATION);
        switch (locationRange) {
            case SI -> {
                Integer subStringValue = subStringValue(0, 2, locationCode);
                productListRequest.setLocationCode(subStringValue);
            }
            case GU -> {
                Integer subStringValue = subStringValue(0, 5, locationCode);
                productListRequest.setLocationCode(subStringValue);
            }
        }

        productList = productMapper.findProductList(productListRequest);
        return productList;
    }

    public ProductDetailResponse getProduct(Long userCode, Long productCode) {
        Optional<ProductDetail> productDetailOptional = productMapper.findProductDetailByProductCode(productCode);
        if (productDetailOptional.isEmpty()) throw new BusinessException(ErrorCode.NOT_FOUND_PRODUCT_DETAIL);

        ProductDetail productDetail = productDetailOptional.get();
        Long sellerCode = productDetail.getUserCode();
        Optional<User> sellerOptional = userMapper.findUserByUserCode(sellerCode);
        if(sellerOptional.isEmpty()) throw new BusinessException(ErrorCode.NOT_FOUND_SELLER);
        return ProductDetailResponse.to(sellerOptional.get(), productDetail);
    }

    private Integer subStringValue(int beginIndex, int endIndex, Integer value) {
        String strValue = Integer.toString(value);
        return Integer.parseInt(strValue.substring(beginIndex, endIndex));
    }

}
