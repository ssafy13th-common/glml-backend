package com.ssafy.a705.tracking.presentation.dto.response;

public record TrackingInfoRes(
        String trackingId,
        String imageUrl
) {

    public static TrackingInfoRes of(String trackingId, String imageUrl) {
        return new TrackingInfoRes(trackingId, imageUrl);
    }

}
