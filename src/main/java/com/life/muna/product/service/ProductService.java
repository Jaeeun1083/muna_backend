package com.life.muna.product.service;

import com.life.muna.common.error.ErrorCode;
import com.life.muna.common.error.exception.BusinessException;
import com.life.muna.location.domain.Location;
import com.life.muna.location.mapper.LocationMapper;
import com.life.muna.product.domain.LocationRange;
import com.life.muna.product.domain.ProductDetail;
import com.life.muna.product.dto.ProductDetailResponse;
import com.life.muna.product.dto.ProductListRequest;
import com.life.muna.product.dto.ProductListResponse;
import com.life.muna.product.mapper.ProductMapper;
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
    private LocationMapper locationMapper;

    public ProductService(ProductMapper productMapper, UserMapper userMapper, LocationMapper locationMapper) {
        this.productMapper = productMapper;
        this.userMapper = userMapper;
        this.locationMapper = locationMapper;
    }

    public List<ProductListResponse> getProductList(ProductListRequest productListRequest) {
        List<ProductListResponse> productList;
        LocationRange locationRange = LocationRange.fromCode(productListRequest.getLocationRange());

        String locationNm = productListRequest.getLocation();
        if (locationRange != LocationRange.L000 && locationNm == null) {
            throw new BusinessException(ErrorCode.UNPROCESSABLE_PRODUCT_LOCATION);
        }

        if (locationNm != null) {
            String[] tokens = locationNm.split(" ");
            if (tokens.length < 3 || tokens.length > 4) {
                throw new BusinessException(ErrorCode.INVALID_LOCATION);
            }

            String locationSiNm = tokens[0];
            String locationGuNm = tokens[1];
            String locationDongNm = tokens[tokens.length -1];

            Optional<Location> locationOptional = locationMapper.findByLocationNm(locationSiNm, locationGuNm, locationDongNm);
            Location location = locationOptional.orElseThrow(() -> new BusinessException(ErrorCode.INVALID_LOCATION));

            Integer locationCode = switch (locationRange) {
                case L001 -> subStringValue(0, 2, location.getLocationDongCd());
                case L002 -> subStringValue(0, 5, location.getLocationDongCd());
                case L003 -> location.getLocationDongCd();
                default -> null;
            };
            productListRequest.setLocationCode(locationCode);
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
