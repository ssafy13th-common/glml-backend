package com.ssafy.a705.domain.tracking.dto.request;

import com.ssafy.a705.domain.tracking.entity.TrackPoint;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record TrackingCreateReq(
        @Valid
        @NotEmpty(message = "트래킹 지점은 최소 1개 이상이어야 합니다.")
        List<TrackPoint> tracks,

        @NotBlank(message = "사진 정보가 입력되지 않았습니다.")
        String thumbnailImage
) {

}
