package com.ssafy.a705.domain.tracking.dto.response;

import java.util.List;

public record TrackingInfosRes(
        List<TrackingInfoRes> trackingImages
) {


    public static TrackingInfosRes of(List<TrackingS3Url> imageUrls) {
        List<TrackingInfoRes> trackingImages = imageUrls.stream()
                .map(t ->
                        new TrackingInfoRes(
                                t.trackingId(),
                                t.imageUrl()
                        )
                ).toList();
        return new TrackingInfosRes(trackingImages);
    }
}
