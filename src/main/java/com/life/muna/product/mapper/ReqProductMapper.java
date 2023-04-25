package com.life.muna.product.mapper;

import com.life.muna.common.dto.MaxProductInfoResponse;
import com.life.muna.product.domain.ReqProduct;
import com.life.muna.product.dto.ProductShareRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReqProductMapper {
    boolean existsByUserCodeAndProductCode(@Param("userCode") Long userCode, @Param("productCode")  Long productCode);
    MaxProductInfoResponse findMaxRequestProductInfo(@Param("userCode") Long userCode, @Param("pageSize") int pageSize);
    ReqProduct findChatReqByProductCode(@Param("productCode")  Long productCode);
    int findChatReqCountByUserCode(@Param("userCode") Long userCode);
    int findChatReqCountByProductCode(@Param("productCode") Long productCode);
    List<Long> findProductCodeByUserCode(@Param("userCode") Long userCode, @Param("offset") int offset, @Param("pageSize") int pageSize);
    int deleteByUserCodeAndProductCode(@Param("userCode") Long userCode, @Param("productCode") Long productCode);
    int save (ProductShareRequest productShareRequest);
}
