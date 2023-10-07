package com.life.muna.productReq.mapper;

import com.life.muna.common.dto.MaxProductInfoResponse;
import com.life.muna.productReq.domain.ReqProduct;
import com.life.muna.productReq.domain.enums.ReqStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ReqProductMapper {
    MaxProductInfoResponse findMaxRequestProductInfo(@Param("userCode") Long userCode, @Param("pageSize") int pageSize);
    List<ReqProduct> findByRequesterCode(@Param("requesterCode") Long requesterCode);
    List<ReqProduct> findByProductCode(@Param("productCode") Long productCode);
    Optional<ReqProduct> findByRequesterCodeAndProductCode(@Param("requesterCode") Long requesterCode, @Param("productCode") Long productCode);

    boolean existsByRequesterCodeAndProductCode(@Param("requesterCode") Long requesterCode, @Param("productCode") Long productCode);
    int save(ReqProduct reqProduct);
    int saveReqStatus(@Param("reqStatus") ReqStatus reqStatus, @Param("productReqCode") Long productReqCode);
    int saveReqStatusAll(@Param("reqStatus") ReqStatus reqStatus, @Param("productCode") Long productCode);
    void saveReqRead (@Param("productReqCode") Long productReqCode);
    int deleteByRequesterCodeAndProductCode(@Param("requesterCode") Long requesterCode, @Param("productCode") Long productCode);
    int deleteByRequesterCodeAndProductReqCode(@Param("requesterCode") Long requesterCode, @Param("productReqCode") Long productReqCode);
}
