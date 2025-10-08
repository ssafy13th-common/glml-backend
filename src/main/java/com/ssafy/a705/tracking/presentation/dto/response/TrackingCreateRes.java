package com.ssafy.a705.tracking.presentation.dto.response;

public record TrackingCreateRes(String trackingId) {

    public static TrackingCreateRes of(String id) {
        return new TrackingCreateRes(id);
    }

}
