package com.life.muna.like.service;

import com.life.muna.common.dto.MaxProductInfoResponse;
import com.life.muna.common.error.ErrorCode;
import com.life.muna.common.error.exception.BusinessException;
import com.life.muna.like.dto.list.ProductLikeListResponse;
import com.life.muna.like.mapper.ProductLikeMapper;
import com.life.muna.product.dto.detail.ProductDetailResponse;
import com.life.muna.product.mapper.ProductMapper;
import com.life.muna.user.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.life.muna.common.error.ErrorCode.NOT_FOUND_USER;

@Service
public class ProductLikeService {
    private final Logger LOG = LoggerFactory.getLogger(ProductLikeService.class);
    private final static int PAGE_SIZE = 30;

    private final ProductLikeMapper productLikeMapper;
    private final ProductMapper productMapper;
    private UserMapper userMapper;

    public ProductLikeService(ProductLikeMapper productLikeMapper, ProductMapper productMapper, UserMapper userMapper) {
        this.productLikeMapper = productLikeMapper;
        this.productMapper = productMapper;
        this.userMapper = userMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean likeProduct(String emailFromToken, Long productCode) {
       /* Long userCode = userMapper.findByEmail(emailFromToken)
                .orElseThrow(() -> new BusinessException(NOT_FOUND_USER)).getUserCode();

        Optional<ProductDetailResponse> findProductDetailOptional = productMapper.findProductDetailByProductCode(productCode);
        if(findProductDetailOptional.isEmpty()) throw new BusinessException(ErrorCode.NOT_FOUND_PRODUCT_DETAIL);
        ProductDetailResponse findProductDetail = findProductDetailOptional.get();
        if(findProductDetail.getUserCode().equals(userCode)) throw new BusinessException(ErrorCode.CANNOT_REQUEST_MY_LIKE);
        if (!findProductDetail.getProductStatus()) throw new BusinessException(ErrorCode.DISABLED_PRODUCT_LIKE);
        boolean isLiked = productLikeMapper.existsByUserCodeAndProductCode(userCode, productCode);
        if (isLiked) throw new BusinessException(ErrorCode.ALREADY_PRODUCT_LIKE);

        int requestLikeResult = productLikeMapper.save(userCode, productCode);
        if (requestLikeResult == 1) {
            int updateReqCntResult = productMapper.updateLikeCnt(findProductDetail.getProductCode(), findProductDetail.getLikes() + 1);
            return updateReqCntResult == 1;
        } else {*/
            return false;
//        }
    }

    public boolean withdrawLikeProduct(String emailFromToken, Long productCode) {
        Long userCode = userMapper.findByEmail(emailFromToken)
                .orElseThrow(() -> new BusinessException(NOT_FOUND_USER)).getUserCode();

        boolean isExists = productLikeMapper.existsByUserCodeAndProductCode(userCode, productCode);
        if(!isExists) throw new BusinessException(ErrorCode.NOT_FOUND_PRODUCT_LIKE);
        int requestLikeResult = productLikeMapper.deleteByUserCodeAndProductCode(userCode, productCode);
        Optional<ProductDetailResponse> findProductDetailOptional = productMapper.findProductDetailByProductCode(productCode);
        if(findProductDetailOptional.isPresent()) {
            ProductDetailResponse productDetailResponse = findProductDetailOptional.get();
            productMapper.updateLikeCnt(productDetailResponse.getProductCode(), productDetailResponse.getReqCnt() > 0 ? productDetailResponse.getReqCnt() - 1 : productDetailResponse.getReqCnt());
        }
        return requestLikeResult != 0;
    }

    public List<ProductLikeListResponse> getProductLiked(String emailFromToken) {
        Long userCode = userMapper.findByEmail(emailFromToken)
                .orElseThrow(() -> new BusinessException(NOT_FOUND_USER)).getUserCode();

//        int offset = (Math.max(page - 1, 0)) * PAGE_SIZE;
//        List<Long> productCodeList = productLikeMapper.findProductCodeByUserCode(userCode, offset, PAGE_SIZE, maxProductCode);
        List<Long> productCodeList = productLikeMapper.findProductCodeByUserCode(userCode);

        return Optional.ofNullable(productCodeList)
                .map(List::stream)
                .orElseGet(Stream::empty)
                .flatMap(productCode -> productMapper.findProductByProductCode(productCode)
                        .map(ProductLikeListResponse::of)
                        .stream())
                .collect(Collectors.toList());
    }

    public MaxProductInfoResponse getMaxProductLikeInfo(String emailFromToken) {
        Long userCode = userMapper.findByEmail(emailFromToken)
                .orElseThrow(() -> new BusinessException(NOT_FOUND_USER)).getUserCode();
        return productLikeMapper.findMaxProductLikeInfo(userCode, PAGE_SIZE);
    }

}
