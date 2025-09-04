package com.ssafy.a705.domain.tracking.dto.response;

public record TrackingInfoRes(
        String trackingId,
        String imageUrl
) {

    public static TrackingInfoRes of(String trackingId, String imageUrl) {
        return new TrackingInfoRes(trackingId, imageUrl);
    }

}
