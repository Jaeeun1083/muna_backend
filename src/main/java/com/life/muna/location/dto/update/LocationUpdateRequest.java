package com.life.muna.location.dto.update;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class LocationUpdateRequest {
    @ApiModelProperty(example = "서울특별시 은평구 신사제1동")
    @NotBlank(message = "위치는 필수 입력 값 입니다.")
    private String location;
}
