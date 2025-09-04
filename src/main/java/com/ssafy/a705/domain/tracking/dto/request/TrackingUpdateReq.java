package com.ssafy.a705.domain.tracking.dto.request;

import java.util.List;

public record TrackingUpdateReq(
        List<String> images,
        String thumbnailImage
) {

}
