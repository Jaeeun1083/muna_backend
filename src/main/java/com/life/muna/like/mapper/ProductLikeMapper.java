package com.life.muna.like.mapper;

import com.life.muna.product.dto.ProductShareRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductLikeMapper {
    boolean existsByUserCodeAndProductCode(@Param("userCode") Long userCode, @Param("productCode") Long productCode);
    int findProductLikeByUserCode(@Param("userCode") Long userCode);
    int findProductLikeCountByProductCode(@Param("productCode") Long productCode);
    List<Long> findProductCodeByUserCode(@Param("userCode") Long userCode, @Param("startProductCode") int startProductCode, @Param("productDataCnt") int productDataCnt);
    int save (ProductShareRequest productShareRequest);
    int deleteByUserCodeAndProductCode(@Param("userCode") Long userCode, @Param("productCode") Long productCode);
}
