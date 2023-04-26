package com.life.muna.like.mapper;

import com.life.muna.common.dto.MaxProductInfoResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductLikeMapper {
    boolean existsByUserCodeAndProductCode(@Param("userCode") Long userCode, @Param("productCode") Long productCode);
    MaxProductInfoResponse findMaxProductLikeInfo(@Param("userCode") Long userCode, @Param("pageSize") int pageSize);
    int findProductLikeByUserCode(@Param("userCode") Long userCode);
    int findProductLikeCountByProductCode(@Param("productCode") Long productCode);
    List<Long> findProductCodeByUserCode(@Param("userCode") Long userCode);
    int save(@Param("userCode") Long userCode, @Param("productCode") Long productCode);
    int deleteByUserCodeAndProductCode(@Param("userCode") Long userCode, @Param("productCode") Long productCode);
}
