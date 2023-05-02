package com.life.muna.product.mapper;

import com.life.muna.product.domain.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductHistoryMapper {
    List<Product> findReceivedProductByUserCode(@Param("userCode") Long userCode);
}
