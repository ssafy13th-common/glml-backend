package com.ssafy.a705.domain.location._color.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ColorUpdateReq(
        @NotBlank(message = "색상이 입력되지 않았습니다.")
        String color
) {

}