package com.life.muna.product.service;

import com.life.muna.common.dto.MaxProductInfoResponse;
import com.life.muna.common.error.ErrorCode;
import com.life.muna.common.error.exception.BusinessException;
import com.life.muna.product.domain.Product;
import com.life.muna.product.dto.ProductHistoryResponse;
import com.life.muna.product.dto.ProductShareRequest;
import com.life.muna.product.mapper.ProductMapper;
import com.life.muna.product.mapper.ReqProductMapper;
import com.life.muna.user.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReqProductService {
    private static final int MAX_REQUEST = 5;
    private final static int PAGE_SIZE = 30;
    private final ProductMapper productMapper;
    private final ReqProductMapper reqProductMapper;
    private final UserMapper userMapper;

    public ReqProductService(ProductMapper productMapper, ReqProductMapper reqProductMapper, UserMapper userMapper) {
        this.productMapper = productMapper;
        this.reqProductMapper = reqProductMapper;
        this.userMapper = userMapper;
    }

    public int getMyRequestCount(String emailFromToken) {
        Long userCode = userMapper.findUserCodeByEmail(emailFromToken);
        int requested = reqProductMapper.findChatReqCountByUserCode(userCode);
        if (requested > MAX_REQUEST) return 0;
        return MAX_REQUEST - requested;
    }

    public Boolean requestShareProduct(String emailFromToken, Long productCode, ProductShareRequest productShareRequest) {

        Long userCode = userMapper.findUserCodeByEmail(emailFromToken);
        productShareRequest.setRequestInfo(userCode, productCode);

        Optional<Product> findProductOptional = productMapper.findProductByProductCode(productCode);
        if(findProductOptional.isEmpty()) throw new BusinessException(ErrorCode.NOT_FOUND_PRODUCT_DETAIL);
        Product findProduct = findProductOptional.get();

        if(findProduct.getUserCode().equals(userCode)) throw new BusinessException(ErrorCode.CANNOT_REQUEST_MY_PRODUCT);

        if (!findProduct.getProductStatus()) throw new BusinessException(ErrorCode.DISABLED_PRODUCT_REQUEST);
        boolean isRequested = reqProductMapper.existsByUserCodeAndProductCode(userCode, productCode);
        if (isRequested) throw new BusinessException(ErrorCode.ALREADY_PRODUCT_REQUEST);

        int requested = reqProductMapper.findChatReqCountByUserCode(userCode);
        if (requested >= MAX_REQUEST) throw new BusinessException(ErrorCode.EXCEED_PRODUCT_REQUEST_COUNT);
        int requestChatResult = reqProductMapper.save(productShareRequest);
        if (requestChatResult == 1) {
            int updateReqCntResult = productMapper.updateReqCnt(findProduct.getProductCode(), findProduct.getReqCnt() + 1);
            return updateReqCntResult == 1;
        } else {
            return false;
        }
    }

    public boolean withdrawRequestProduct(String emailFromToken, Long productCode) {
        Long userCode = userMapper.findUserCodeByEmail(emailFromToken);
        boolean isExists = reqProductMapper.existsByUserCodeAndProductCode(userCode, productCode);
        if(!isExists) throw new BusinessException(ErrorCode.NOT_FOUND_PRODUCT_REQ);
        int requestChatResult = reqProductMapper.deleteByUserCodeAndProductCode(userCode, productCode);
        Optional<Product> findProductOptional = productMapper.findProductByProductCode(productCode);
        if(findProductOptional.isPresent()) {
            Product product = findProductOptional.get();
            productMapper.updateReqCnt(product.getProductCode(), product.getReqCnt() > 0 ? product.getReqCnt() - 1 : product.getReqCnt());
        }
        return requestChatResult != 0;
    }

    public List<ProductHistoryResponse> getRequestProductList(String emailFromToken) {
        Long userCode = userMapper.findUserCodeByEmail(emailFromToken);
//        int offset = (Math.max(page - 1, 0)) * PAGE_SIZE;
        List<Long> productCodeList = reqProductMapper.findProductCodeByUserCode(userCode);
        List<ProductHistoryResponse> productListResponses = new ArrayList<>();
        for (long productCode: productCodeList) {
            Optional<Product> findProductOptional = productMapper.findProductByProductCode(productCode);
            if (findProductOptional.isEmpty()) {

            } else {
                int reqCnt = reqProductMapper.findChatReqCountByProductCode(productCode);
                ProductHistoryResponse response = ProductHistoryResponse.of(findProductOptional.get(), reqCnt);
                productListResponses.add(response);
            }
        }
        return productListResponses;
    }

    public MaxProductInfoResponse getMaxRequestProductInfo(Long userCode) {
        return reqProductMapper.findMaxRequestProductInfo(userCode, PAGE_SIZE);
    }

}
