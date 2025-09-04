package com.ssafy.a705.domain.tracking.dto.response;

import com.ssafy.a705.domain.tracking.entity.TrackPoint;
import com.ssafy.a705.domain.tracking.entity.Tracking;
import java.time.LocalDateTime;
import java.util.List;

public record TrackingDetailRes(
        String trackingId,
        List<TrackPoint> trackPoints,
        List<String> images,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {

    public static TrackingDetailRes from(Tracking tracking, List<String> images) {
        return new TrackingDetailRes(tracking.getId(), tracking.getTracks(), images,
                tracking.getCreatedAt(), tracking.getModifiedAt());
    }

}
