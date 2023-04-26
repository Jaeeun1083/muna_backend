package com.life.muna.location.service;

import com.life.muna.common.error.ErrorCode;
import com.life.muna.common.error.exception.BusinessException;
import com.life.muna.location.domain.Location;
import com.life.muna.location.dto.LocationUpdateRequest;
import com.life.muna.location.dto.LocationUpdateResponse;
import com.life.muna.location.mapper.LocationMapper;
import com.life.muna.user.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
public class LocationService {
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    private static final int MIN_TOKENS = 3;
    private static final int MAX_TOKENS = 5;
    private static final String DELIMITER = " ";

    private final LocationMapper locationMapper;
    private final UserMapper userMapper;

    public LocationService(LocationMapper locationMapper, UserMapper userMapper) {
        this.locationMapper = locationMapper;
        this.userMapper = userMapper;
    }

    public LocationUpdateResponse updateLocation (String emailFromToken, LocationUpdateRequest locationUpdateRequest) {
        Long userCode = userMapper.findUserCodeByEmail(emailFromToken);
//        validateEmailFromTokenAndUserCode(emailFromToken, locationUpdateRequest.getUserCode());
        Location location = findLocation(locationUpdateRequest.getLocation());
        LOG.info("Update Location user : {}", emailFromToken);
        LOG.info("Update Location 시: {}, 구: {}, 동: {}, 동 하위: {} ", location.getLocationSiNm(), location.getLocationGuNm(), location.getLocationDongNm(), location.getLocationDongSubNm());
        userMapper.saveLocation(userCode, location.getLocationDongCd());
        return LocationUpdateResponse.of(location);
    }

    private Location findLocation (String locationName) {
        String[] tokens = Arrays.stream(locationName.split(DELIMITER))
                .filter(token -> !token.isBlank())
                .toArray(String[]::new);

        if (tokens.length < MIN_TOKENS || tokens.length > MAX_TOKENS) {
            throw new BusinessException(ErrorCode.INVALID_LOCATION_PARAMETER);
        }

        String locationSiNm = tokens[0];
        String locationGuNm = tokens[1];
        String tempLocationGuNm = String.join(DELIMITER, locationGuNm, tokens[2]);
        String locationDongNm =  tokens.length == MAX_TOKENS ?  tokens[3]: tokens[2];
        String locationDongSubNm = tokens.length == MAX_TOKENS ? tokens[4] : null;

        Optional<Location> location = switch (tokens.length) {
            case 3 -> {
                Optional<Location> locationOptional = locationMapper.findByLocationNm(locationSiNm, locationGuNm, locationDongNm);
                if (locationOptional.isPresent()) {
                    yield locationOptional;
                } else {
                    yield locationMapper.findByLocationNm(locationSiNm, locationGuNm, subStringBeforeDigit(locationDongNm));
                }
            }
            case 4 -> {
                locationDongSubNm = tokens[3];
                int count = locationMapper.countByLocationGuNm(tempLocationGuNm);
                if (count >= 1) {
                    Optional<Location> locationOptional = locationMapper.findByLocationNm(locationSiNm, tempLocationGuNm, locationDongSubNm);
                    if (locationOptional.isPresent()) {
                        yield locationOptional;
                    } else {
                        yield locationMapper.findByLocationNm(locationSiNm, locationGuNm, subStringBeforeDigit(locationDongSubNm));
                    }
                } else {
                    locationDongSubNm = tokens[3];
                    Optional<Location> locationOptional =  locationMapper.findByLocationNmWithSub(locationSiNm, locationGuNm, locationDongNm, locationDongSubNm);
                    if (locationOptional.isPresent()) {
                        yield locationOptional;
                    } else {
                        yield locationMapper.findByLocationNmWithSub(locationSiNm, locationGuNm, locationDongNm, subStringBeforeDigit(locationDongSubNm));
                    }
                }
            }
            case 5 -> {
                Optional<Location> locationOptional =  locationMapper.findByLocationNmWithSub(locationSiNm, tempLocationGuNm, locationDongNm, locationDongSubNm);
                if (locationOptional.isPresent()) {
                    yield locationOptional;
                } else {
                    yield locationMapper.findByLocationNmWithSub(locationSiNm, locationGuNm, locationDongNm, subStringBeforeDigit(locationDongSubNm));
                }
            }
            default ->  throw new BusinessException(ErrorCode.INVALID_LOCATION_PARAMETER);
        };
        return location.orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_LOCATION));
    }

    private String subStringBeforeDigit(String input) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            if (Character.isDigit(input.charAt(i))) {
                break;
            }
            output.append(input.charAt(i));
        }
        System.out.println("output : " + output);
        return String.valueOf(output);
    }

    public static String getLocationName(Location location) {
        return location.getLocationSiNm() + DELIMITER + location.getLocationGuNm() + DELIMITER + location.getLocationDongNm()
                + (location.getLocationDongSubNm() != null ? (DELIMITER + location.getLocationDongSubNm()) : null);
    }

}
