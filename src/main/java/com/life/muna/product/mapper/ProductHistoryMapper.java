package com.life.muna.product.mapper;

import com.life.muna.product.domain.Product;
import com.life.muna.product.domain.ProductHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductHistoryMapper {
    List<Product> findReceivedProductBySellerCode(@Param("sellerCode") Long sellerCode);
    List<Product> findReceivedProductByBuyerCode(@Param("buyerCode") Long buyerCode);
    int countBySellerCode(@Param("sellerCode") Long sellerCode);
    int save(ProductHistory productHistory);
}
