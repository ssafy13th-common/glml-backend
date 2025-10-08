package com.ssafy.a705.tracking.presentation.dto.response;

public record TrackingS3Url(
        String trackingId,
        String imageUrl
) {

    public static TrackingS3Url of(String trackingId, String imageUrl) {
        return new TrackingS3Url(trackingId, imageUrl);
    }
}
