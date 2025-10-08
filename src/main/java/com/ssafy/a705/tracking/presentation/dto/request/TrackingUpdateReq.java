package com.ssafy.a705.tracking.presentation.dto.request;

import java.util.List;

public record TrackingUpdateReq(
        List<String> images,
        String thumbnailImage
) {

}
