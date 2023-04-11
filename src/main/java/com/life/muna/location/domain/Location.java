package com.life.muna.location.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Location {
    private Integer locationSiCd;
    private String locationSiNm;
    private Integer locationGuCd;
    private String locationGuNm;
    private Integer locationDongCd;
    private String locationDongNm;

    @Builder
    public Location(Integer locationSiCd, String locationSiNm, Integer locationGuCd, String locationGuNm, Integer locationDongCd, String locationDongNm) {
        this.locationSiCd = locationSiCd;
        this.locationSiNm = locationSiNm;
        this.locationGuCd = locationGuCd;
        this.locationGuNm = locationGuNm;
        this.locationDongCd = locationDongCd;
        this.locationDongNm = locationDongNm;
    }
}
