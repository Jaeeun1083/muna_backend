package com.life.muna.product.mapper;

import com.life.muna.product.domain.Product;
import com.life.muna.product.domain.ProductDetail;
import com.life.muna.product.dto.ProductDetailResponse;
import com.life.muna.product.dto.ProductListRequest;
import com.life.muna.product.dto.ProductListResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ProductMapper {
    List<ProductListResponse> findProductList(ProductListRequest productListRequest);
    Optional<Product> findProductByProductCode(@Param("productCode") Long productCode);
    List<Product> findProductByUserCode(@Param("userCode") Long userCode, @Param("offset") int offset, @Param("pageSize") int pageSize);
    Optional<ProductDetailResponse> findProductDetailByProductCode(@Param("productCode") Long productCode);
    int updateReqCnt(@Param("productCode") Long productCode, @Param("reqCnt") int reqCnt);
    int updateLikeCnt(@Param("productCode") Long productCode, @Param("likes") int likes);
    int saveProductDetail(ProductDetail productDetail);
    int save(Product product);
}
