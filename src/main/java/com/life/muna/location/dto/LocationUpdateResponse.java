package com.life.muna.location.dto;

import com.life.muna.location.domain.Location;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LocationUpdateResponse {
    private static final String DELIMITER = " ";
    private Long locationDongCd;
    private String location;

    @Builder
    public LocationUpdateResponse(Long locationDongCd, String location) {
        this.locationDongCd = locationDongCd;
        this.location = location;
    }

    public static LocationUpdateResponse of(Location location) {
        String locationName;
        if (location.getLocationDongSubNm().isEmpty()) {
            locationName = String.join(DELIMITER, location.getLocationSiNm(), location.getLocationGuNm(), location.getLocationDongNm());
        } else {
            locationName = String.join(DELIMITER, location.getLocationSiNm(), location.getLocationGuNm(), location.getLocationDongNm(), location.getLocationDongSubNm());
        }
         return LocationUpdateResponse.builder()
                .locationDongCd(location.getLocationDongCd())
                .location(locationName)
                .build();
    }

}
