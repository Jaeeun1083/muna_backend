package com.life.muna.user.mapper;

import com.life.muna.user.domain.TempKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Mapper
@Repository
public interface TempKeyMapper {
    Optional<TempKey> findByPhone(@Param("phone") String phone);
    int updateTempKey(TempKey tempKey);
    int save(TempKey tempKey);
}
