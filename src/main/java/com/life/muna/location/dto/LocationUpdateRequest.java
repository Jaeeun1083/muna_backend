package com.life.muna.location.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class LocationUpdateRequest {
    @ApiModelProperty(example = "25", required = true)
    @NotNull(message = "유저 코드는 필수 입력 값 입니다.")
    private Long userCode;
    @ApiModelProperty(example = "서울특별시 은평구 신사제1동")
    @NotBlank(message = "위치는 필수 입력 값 입니다.")
    private String location;
}
