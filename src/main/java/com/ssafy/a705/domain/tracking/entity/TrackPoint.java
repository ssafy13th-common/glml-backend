package com.ssafy.a705.domain.tracking.entity;

import jakarta.validation.constraints.NotNull;

public record TrackPoint(
        @NotNull(message = "위도 정보가 입력되지 않았습니다.")
        Double latitude,

        @NotNull(message = "경도 정보가 입력되지 않았습니다.")
        Double longitude,

        @NotNull(message = "시간 정보가 입력되지 않았습니다.")
        Long timestamp
) {

}