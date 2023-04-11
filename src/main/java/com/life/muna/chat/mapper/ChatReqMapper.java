package com.life.muna.chat.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ChatReqMapper {
    boolean existsByUserCodeAndProductCode(@Param("userCode") Long userCode, @Param("productCode")  Long productCode);
}
