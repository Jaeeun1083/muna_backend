package com.life.muna.location.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Location {
    private Integer locationSiCd;
    private String locationSiNm;
    private Integer locationGuCd;
    private String locationGuNm;
    private Long locationDongCd;
    private String locationDongNm;
    private String locationDongSubNm;
    private float locationLatitude;
    private float locationLongitude;

    @Builder
    public Location(Integer locationSiCd, String locationSiNm, Integer locationGuCd, String locationGuNm, Long locationDongCd, String locationDongNm, String locationDongSubNm, float locationLatitude, float locationLongitude) {
        this.locationSiCd = locationSiCd;
        this.locationSiNm = locationSiNm;
        this.locationGuCd = locationGuCd;
        this.locationGuNm = locationGuNm;
        this.locationDongCd = locationDongCd;
        this.locationDongNm = locationDongNm;
        this.locationDongSubNm = locationDongSubNm;
        this.locationLatitude = locationLatitude;
        this.locationLongitude = locationLongitude;
    }

}
