package com.life.muna.product.mapper;

import com.life.muna.common.dto.MaxProductInfoResponse;
import com.life.muna.product.domain.Product;
import com.life.muna.product.domain.ProductDetail;
import com.life.muna.product.domain.enums.ProductStatus;
import com.life.muna.product.dto.detail.ProductDetailResponse;
import com.life.muna.product.dto.list.ProductListRequest;
import com.life.muna.product.dto.list.ProductListResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ProductMapper {
    MaxProductInfoResponse findMaxProductInfo(@Param("pageSize") int pageSize);
    List<ProductListResponse> findProductList(ProductListRequest productListRequest);
    Optional<Product> findProductByProductCode(@Param("productCode") Long productCode);
    List<Product> findProductByUserCode(@Param("userCode") Long userCode);
    Optional<ProductDetailResponse> findProductDetailByProductCode(@Param("productCode") Long productCode);
    int updateReqCnt(@Param("productCode") Long productCode, @Param("reqCnt") int reqCnt);
    int updateLikeCnt(@Param("productCode") Long productCode, @Param("likes") int likes);
    void updateViewCnt(@Param("productCode") Long productCode, @Param("views") int views);
    void updateDate(@Param("productCode") Long productCode);
    int updateProductStatus(@Param("productCode") Long productCode, @Param("productStatus") ProductStatus productStatus);

    int saveProduct(Product product);
    int saveProductDetail(ProductDetail productDetail);
}
