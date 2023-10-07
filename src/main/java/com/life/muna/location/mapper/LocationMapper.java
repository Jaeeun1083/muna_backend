package com.life.muna.location.mapper;

import com.life.muna.location.domain.Location;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface LocationMapper {
    Optional<Location> findByLocationNm(@Param("locationSiNm") String locationSiNm, @Param("locationGuNm") String locationGuNm, @Param("locationDongNm") String locationDongNm);
    Optional<Location> findByLocationNmWithSub(@Param("locationSiNm") String locationSiNm, @Param("locationGuNm") String locationGuNm, @Param("locationDongNm") String locationDongNm,  @Param("locationDongSubNm") String locationDongSubNm);
    Location findByLocationDongCd(@Param("locationDongCd") Long locationDongCd);
    List<String> findNotEndingWithGuOrGun();
    int countByLocationGuNm(@Param("locationGuNm") String locationGuNm);
}
