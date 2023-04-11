package com.life.muna.chat.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChatReqMapper {
    boolean existsByUserCodeAndProductCode(Long userCode, Long productCode);
}
