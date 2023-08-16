package com.life.muna.mcoin.mapper;

import com.life.muna.mcoin.domain.Mcoin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface McoinMapper {
    Mcoin findByUserCode(@Param("userCode") Long userCode);
    int updateMcoin(Mcoin mcoin);
    int save(@Param("userCode") Long userCode);
}

