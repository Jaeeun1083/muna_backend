package com.life.muna.location.mapper;

import com.life.muna.location.domain.Location;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface LocationMapper {
    Optional<Location> findByLocationNm(String locationSiNm, String locationGuNm, String locationDongNm);
}
