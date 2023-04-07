package com.life.muna.product.mapper;

import com.life.muna.product.domain.Product;
import com.life.muna.product.domain.ProductDetail;
import com.life.muna.product.dto.ProductListRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ProductMapper {
    List<Product> findProductList(ProductListRequest productListRequest);
    Optional<ProductDetail> findProductDetailByProductCode(Long productCode);

}
