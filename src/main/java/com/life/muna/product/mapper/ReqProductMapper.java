package com.life.muna.product.mapper;

import com.life.muna.product.domain.ReqProduct;
import com.life.muna.product.dto.ProductShareRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReqProductMapper {
    ReqProduct findChatReqByProductCode(@Param("productCode")  Long productCode);
    boolean existsByUserCodeAndProductCode(@Param("userCode") Long userCode, @Param("productCode")  Long productCode);
    int findChatReqCountByUserCode(@Param("userCode") Long userCode);
    List<Long> findProductCodeByUserCode(@Param("userCode") Long userCode, @Param("startProductCode") int startProductCode, @Param("productDataCnt") int productDataCnt);
    int deleteByUserCodeAndProductCode(@Param("userCode") Long userCode, @Param("productCode") Long productCode);
    int save (ProductShareRequest productShareRequest);
}
